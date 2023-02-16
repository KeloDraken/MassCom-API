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
    @ManyToOne
    @JoinColumn(name = "emailMessageId")
    private EmailMessage emailMessage;
    private boolean isReceived;
    private java.sql.Timestamp datetimeReceived;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUserId(User user) {
        this.user = user;
    }

    public EmailMessage getEmailMessage() {
        return this.emailMessage;
    }

    public void setEmailMessage(EmailMessage emailMessage) {
        this.emailMessage = emailMessage;
    }

    public boolean isReceived() {
        return this.isReceived;
    }

    public void setIsReceived(boolean received) {
        this.isReceived = received;
    }

    public Timestamp getDatetimeReceived() {
        return this.datetimeReceived;
    }

    public void setDatetimeReceived(Timestamp datetimeReceived) {
        this.datetimeReceived = datetimeReceived;
    }
}
