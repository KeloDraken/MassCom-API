package com.example.accessingdatajpa.entities;


import jakarta.persistence.*;

@Entity
public class EmailMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String body;
    private String emailSubject;
    private java.sql.Timestamp sentDate;

    @OneToOne
    @JoinColumn(name = "senderId")
    private User sender;

    @OneToOne
    @JoinColumn(name = "recipientId")
    private User recipient;
    private String hasAttachments;
    private boolean isDraft;
    private boolean isDeleted;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }


    public java.sql.Timestamp getSentDate() {
        return sentDate;
    }

    public void setSentDate(java.sql.Timestamp sentDate) {
        this.sentDate = sentDate;
    }


    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }


    public String getHasAttachments() {
        return hasAttachments;
    }

    public void setHasAttachments(String hasAttachments) {
        this.hasAttachments = hasAttachments;
    }

    public boolean isDraft() {
        return isDraft;
    }

    public void setDraft(boolean draft) {
        isDraft = draft;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
