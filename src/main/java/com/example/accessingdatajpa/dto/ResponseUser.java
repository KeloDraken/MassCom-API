package com.example.accessingdatajpa.dto;

import com.example.accessingdatajpa.entities.User;
import com.example.accessingdatajpa.entities.UserType;

public class ResponseUser {
    private final User user;
    private final UserType userType;

    public ResponseUser(User user, UserType userType) {
        this.user = user;
        this.userType = userType;
    }

    public User getUser() {
        return user;
    }

    public String getUserType() {
        return userType.getUserType();
    }
}
