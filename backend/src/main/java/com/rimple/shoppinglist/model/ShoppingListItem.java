package com.rimple.shoppinglist.model;

import java.math.BigDecimal;

public class ShoppingListItem {
    private String id;
    private String item;
    private BigDecimal price;
    private boolean pickedUp;

    public ShoppingListItem(String id, String item, BigDecimal price) {
        this.id = id;
        this.item = item;
        this.price = price;
        this.pickedUp = false;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isPickedUp() {
        return pickedUp;
    }

    public void setPickedUp(boolean pickedUp) {
        this.pickedUp = pickedUp;
    }
}
