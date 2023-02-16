package com.example.accessingdatajpa.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @Nullable
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String emailAddress;
    private String userName;
    private String userSurname;

    private boolean isDeleted = false;
    @Nullable
    private Timestamp dateJoined;
    @ManyToOne
    @JoinColumn(name = "propertyId")
    private Property propertyId;

    public User(String userName, String userSurname, String emailAddress) {
        this.userName = userName;
        this.userSurname = userSurname;
        this.emailAddress = emailAddress;
    }

    protected User() {
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }


    public Timestamp getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Timestamp dateJoined) {
        this.dateJoined = dateJoined;
    }


    public Property getProperty() {
        return propertyId;
    }

    public void setProperty(Property propertyId) {
        this.propertyId = propertyId;
    }

}
