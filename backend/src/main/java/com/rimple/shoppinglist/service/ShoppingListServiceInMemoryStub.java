package com.rimple.shoppinglist.service;

import com.rimple.shoppinglist.model.ShoppingListItem;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A really naive implementation just to stub up the controller.
 * Meant to be replaced with the actual implementation.
 */
@Service
public class ShoppingListServiceInMemoryStub implements ShoppingListService {

    public ShoppingListServiceInMemoryStub() {

    }

    private final Map<String, ShoppingListItem> shoppingList = new HashMap<>();

    @Override
    public List<ShoppingListItem> listItems(String listKey) {
        return this.shoppingList.values().stream().collect(Collectors.toList());
    }

    @Override
    public String addItem(String listKey, ShoppingListItem item) {
        String itemKey = UUID.randomUUID().toString();
        item.setId(itemKey);
        this.shoppingList.put(itemKey, item);
        return itemKey;
    }

    @Override
    public ShoppingListItem getItem(String listKey, String itemKey) {
        return shoppingList.get(itemKey);
    }

    @Override
    public void updateItem(String listKey, ShoppingListItem updatedItem) {
        shoppingList.put(updatedItem.getId(), updatedItem);
    }

    @Override
    public void deleteItem(String listKey, String itemKey) {
        if (!shoppingList.containsKey(itemKey)) {
            throw new RuntimeException("key not found " + itemKey);
        }
        shoppingList.remove(itemKey);
    }

    @Override
    public void removeAllItems(String listKey) {
        this.shoppingList.clear();

    }
}
