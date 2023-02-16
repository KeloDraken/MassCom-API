package com.example.accessingdatajpa.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Property implements Comparable<Property> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Nullable
    private String propertyName;
    @Nullable
    private String propertyAddress;
    private boolean isDeleted = false;

    protected Property() {
    }

    public Property(String propertyName, String propertyAddress) {
        this.propertyName = propertyName;
        this.propertyAddress = propertyAddress;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPropertyName() {
        return this.propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyAddress() {
        return this.propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    @Override
    public int compareTo(Property property) {
        return this.getId().compareTo(property.getId());
    }
}
