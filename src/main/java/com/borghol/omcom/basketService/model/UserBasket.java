package com.borghol.omcom.basketService.model;

import java.util.ArrayList;
import java.util.List;

public class UserBasket {
    private String id;
    private String userId;
    private List<BasketItem> items;

    public UserBasket() {

    }

    public UserBasket(String userId) {
        this.userId = userId;
        this.items = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public List<BasketItem> getItems() {
        List<BasketItem> items = new ArrayList<>();
        for (BasketItem i : this.items){
            items.add(i.clone());
        }
        return items;
    }

    public void addItem(BasketItem item) {
        for (BasketItem i : this.items) {
            if (i.getItemId().equals(item.getItemId())) {
                i.addCount(item.getCount());
                return;
            }
        }
        items.add(item);
    }

    public Boolean removeItem(BasketItem item) {
        for (BasketItem i : this.items) {
            if (i.getItemId().equals(item.getItemId())) {
                return this.items.remove(i);
            }
        }
        return false;
    }

    public Boolean addToItem(String itemId, Integer count) {
        for (BasketItem i : items)
            if (i.getItemId().equals(itemId)) 
                return i.addCount(count);
        
        return false;
    }

    public Boolean removeFromItem(String itemId, Integer count) {
        for (BasketItem i : items) 
            if (i.getItemId().equals(itemId)) {
                return i.removeCount(count);
            }
        return false;
    }



}