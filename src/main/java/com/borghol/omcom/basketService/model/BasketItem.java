package com.borghol.omcom.basketService.model;

public class BasketItem implements Cloneable {
    private String itemId;
    private Integer count;

    public BasketItem(String itemId) {
        this.itemId = itemId;
        this.count = 1;
    }

    private BasketItem(String itemId, Integer count) {
        this.itemId = itemId;
        this.count = count;
    }

    public String getItemId() { return itemId; }
    public Integer getCount() { return count; }
    
    public Boolean addCount(Integer count) {
        if (count > 0) {
            this.count += count;
            return true;
        }            
        return false;
    }

    public Integer addCount() {
        return(++this.count);
    }

    @Override
    public BasketItem clone() {
        return new BasketItem(itemId, count);
    }

	public Boolean removeCount(Integer count) {
        if (count < 0)
            return false;
        
        if (this.count - count < 0)
            this.count = 0;
        else
            this.count -= count;
            
        return true;
	}
}