package com.example.accessingdatajpa.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class UserEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    private long emailMessageId;
    private String received;
    private java.sql.Timestamp datetimeReceived;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public User getUser() {
        return user;
    }

    public void setUserId(User user) {
        this.user = user;
    }


    public long getEmailMessageId() {
        return emailMessageId;
    }

    public void setEmailMessageId(long emailMessageId) {
        this.emailMessageId = emailMessageId;
    }


    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }


    public Timestamp getDatetimeReceived() {
        return datetimeReceived;
    }

    public void setDatetimeReceived(Timestamp datetimeReceived) {
        this.datetimeReceived = datetimeReceived;
    }

}
