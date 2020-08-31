package com.rimple.shoppinglist.service;

import com.rimple.shoppinglist.model.ShoppingListItem;
import java.util.List;

public interface ShoppingListService {
    List<ShoppingListItem> listItems(String listKey);
    String addItem(String listKey, ShoppingListItem item);
    ShoppingListItem getItem(String listKey, String itemKey);
    void updateItem(String listKey, ShoppingListItem item);
    void deleteItem(String listKey, String itemKey);
    void removeAllItems(String listKey);
}
