package com.example.accessingdatajpa.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String emailAddress;
    private String userName;
    private String userSurname;
    private java.sql.Timestamp dateJoined;

    @ManyToOne
    @JoinColumn(name = "propertyId")
    private Property propertyId;


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


    public java.sql.Timestamp getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(java.sql.Timestamp dateJoined) {
        this.dateJoined = dateJoined;
    }


    public Property getProperty() {
        return propertyId;
    }

    public void setProperty(Property propertyId) {
        this.propertyId = propertyId;
    }

}
