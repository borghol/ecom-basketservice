package com.borghol.omcom.basketService.model;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserBasketItem {
    private String userId;
    private String itemId;
    private Integer count;
    private Double measurement;
    
    // Basket/Wishlist
    private ItemLocation location;
    private Date dateUpdated;

    // User/Visitor
    private UserType userType;
}