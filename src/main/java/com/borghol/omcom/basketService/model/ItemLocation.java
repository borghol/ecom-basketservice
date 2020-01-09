package com.borghol.omcom.basketService.model;

public enum ItemLocation {
    BASKET("basket"),
    WISHLIST("wishlist");

    private String itemLocation;

    ItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getItemLocation() {
        return itemLocation;
    }
}