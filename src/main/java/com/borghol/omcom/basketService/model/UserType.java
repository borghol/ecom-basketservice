package com.borghol.omcom.basketService.model;

public enum UserType {
    VISITOR("visitor"),
    USER("user");

    private String userType;

    UserType(String type) {
        this.userType = type;
    }

    public String getUserType() {
        return userType;
    }
}