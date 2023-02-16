package com.example.accessingdatajpa.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "Users")
public class User implements Comparable<User> {
    @Id
    @Nullable
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return this.userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public Timestamp getDateJoined() {
        return this.dateJoined;
    }

    public void setDateJoined(Timestamp dateJoined) {
        this.dateJoined = dateJoined;
    }

    public Property getProperty() {
        return this.propertyId;
    }

    public void setProperty(Property propertyId) {
        this.propertyId = propertyId;
    }

    @Override
    public int compareTo(User user) {
        return this.getId().compareTo(user.getId());
    }
}
