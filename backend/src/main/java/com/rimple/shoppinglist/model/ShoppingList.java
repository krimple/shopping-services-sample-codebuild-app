package com.rimple.shoppinglist.model;

import java.util.List;

public class ShoppingList {

    private String listId;
    private List<ShoppingListItem> items;

    public ShoppingList(String listId) {
        this.listId = listId;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public List<ShoppingListItem> getItems() {
        return items;
    }

    public void setItems(List<ShoppingListItem> items) {
        this.items = items;
    }
}
