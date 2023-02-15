package com.example.accessingdatajpa.dto;

public class CreateProperty {
    private final String propertyName;
    private final String propertyAddress;

    public CreateProperty(String propertyName, String propertyAddress) {
        this.propertyName = propertyName;
        this.propertyAddress = propertyAddress;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }
}
