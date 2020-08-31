package com.rimple.shoppinglist.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;
import com.rimple.shoppinglist.model.ShoppingListItem;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service()
@Qualifier("dynamoDBService")
public class ShoppingListDynamoDBService implements ShoppingListService {

    private AmazonDynamoDB db;

    @Value("${SHOPPING_LIST_TABLE_NAME:RimpleShoppingServicesShoppingList}")
    private String tableName;


    public ShoppingListDynamoDBService() {
        db = AmazonDynamoDBClientBuilder.defaultClient();
    }

    @Override
    public List<ShoppingListItem> listItems(String listKey) {
        HashMap<String, AttributeValue> queryValues =
                new HashMap<String, AttributeValue>();
        queryValues.put(":OwnerId", new AttributeValue().withS(listKey));

        try {
            //GetItemRequest request = new GetItemRequest(tableName, item_values);
            QueryRequest request = new QueryRequest()
                    .withTableName(tableName)
                    .withKeyConditionExpression("OwnerId = :OwnerId")
                    .withExpressionAttributeValues(queryValues);
            QueryResult result = db.query(request);
            if (result == null || result.getCount() == 0) {
                throw new RuntimeException("Row not found or query criteria failed.");
            } else {
                List<ShoppingListItem> itemsFound =
                        result.getItems().stream()
                        .map(i -> new ShoppingListItem(
                                  i.get("ItemId").getS(),
                                  i.get("Item").getS(),
                                  new BigDecimal(i.get("Price").getS())))
                       .collect(Collectors.toList());
                return itemsFound;
            }
        } catch (AmazonServiceException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String addItem(String listKey, ShoppingListItem item) {
        HashMap<String, AttributeValue> item_values =
                new HashMap<String,AttributeValue>();

        String itemKey = UUID.randomUUID().toString();
        item_values.put("OwnerId", new AttributeValue(listKey));
        item_values.put("ItemId", new AttributeValue(itemKey));
        item_values.put("Item", new AttributeValue(item.getItem()));
        item_values.put("Price", new AttributeValue(item.getPrice().toString()));
        item_values.put("Picked", new AttributeValue("F"));
        try {
            db.putItem(tableName, item_values);
        } catch (ResourceNotFoundException e) {
            System.err.format("Error: The table \"%s\" can't be found.\n", tableName);
            System.err.println("Be sure that it exists and that you've typed its name correctly!");
            throw new RuntimeException(e);
        } catch (AmazonServiceException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return itemKey;
    }

    @Override
    public ShoppingListItem getItem(String listKey, String itemKey) {
        HashMap<String, AttributeValue> item_values =
                new HashMap<String, AttributeValue>();
        item_values.put("OwnerId", new AttributeValue(listKey));
        item_values.put("ItemId", new AttributeValue(itemKey));

        GetItemRequest request = new GetItemRequest(tableName, item_values);
        try {

            GetItemResult result = db.getItem(request);
            Map<String, AttributeValue> itemData = result.getItem();
            if (itemData != null) {

                ShoppingListItem returnedItem = new ShoppingListItem(
                        itemKey,
                        itemData.get("Item").getS(),
                        new BigDecimal(itemData.get("Price").getS()));

                return returnedItem;
            } else {
                return null;
            }
        } catch (AmazonServiceException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateItem(String listKey, ShoppingListItem item) {
        var itemToFind = this.getItem(listKey, item.getId());

        UpdateItemRequest request =
                new UpdateItemRequest()
                .withTableName(tableName)
                .addKeyEntry("OwnerId", new AttributeValue().withS(listKey))
                .addKeyEntry("ItemId", new AttributeValue().withS(item.getId()))
                .addAttributeUpdatesEntry("Price",
                    new AttributeValueUpdate().withValue(new AttributeValue().withS(item.getPrice().toString()))
                )
                .addAttributeUpdatesEntry("Item",
                    new AttributeValueUpdate().withValue(new AttributeValue().withS(item.getItem()))
                )
                .addAttributeUpdatesEntry("PickedUp",
                    new AttributeValueUpdate().withValue(new AttributeValue().withBOOL(item.isPickedUp()))
                );
        db.updateItem(request);
    }

    @Override
    public void deleteItem(String listKey, String itemKey) {
        Map<String, AttributeValue> criteria = new HashMap<>();
        criteria.put("OwnerId", new AttributeValue(listKey));
        criteria.put("ItemId", new AttributeValue(itemKey));

        try {
            db.deleteItem(tableName, criteria);
        } catch (AmazonServiceException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Apparently there isn't a better way to delete everything in a table...
     * @param listKey the list owner
     */
    @Override
    public void removeAllItems(String listKey) {

        AtomicInteger batchCount = new AtomicInteger();

        QueryRequest queryRequest = new QueryRequest();
        List<AttributeValue> attributeValues = new ArrayList<>();
        attributeValues.add(new AttributeValue(listKey));

        queryRequest
                .addKeyConditionsEntry("OwnerId", new Condition()
                        .withComparisonOperator(ComparisonOperator.EQ)
                        .withAttributeValueList(attributeValues))
                .setTableName(tableName);
        var search = db.query(queryRequest);

        var batch = new ArrayList<WriteRequest>();

        // search and delete, one query at a time
        // prime the result pump
        var results = search.getItems();
        while (results.stream().count() > 0) {
            int rsSize = results.size();
            results.stream().forEach(item -> {
                batchCount.getAndIncrement();
                var key = item.get("ItemId").getS();
                var keysForDelete = new HashMap<String, AttributeValue>();
                keysForDelete.put("OwnerId", item.get("OwnerId"));
                keysForDelete.put("ItemId", item.get("ItemId"));
                var deleteRequest = new DeleteRequest(keysForDelete);
                var writeRequest = new WriteRequest(deleteRequest);
                batch.add(writeRequest);

                // have we hit 25 or the end of the count? Send 'em off...
                if (rsSize == batchCount.get() || batchCount.get() % 25 == 0) {
                    BatchWriteItemRequest request = new BatchWriteItemRequest();
                    request.addRequestItemsEntry(tableName, batch);
                    // do it
                    db.batchWriteItem(request);
                    batchCount.set(0);
                    batch.clear();
                }
            });
            // after each batch, call this again and see if we have
            // more to do
            results = db.query(queryRequest).getItems();
        }
    }
}
