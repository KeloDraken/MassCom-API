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
    private boolean isDraft = true;
    private boolean isDeleted = false;

    protected EmailMessage() {
    }

    public EmailMessage(User sender, User recipient, String emailSubject, String emailBody) {
        this.body = emailBody;
        this.emailSubject = emailSubject;
        this.sender = sender;
        this.recipient = recipient;
    }

    public long getId() {
        return this.id;
    }

    public String getBody() {
        return this.body;
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
        return this.sentDate;
    }

    public void setSentDate(java.sql.Timestamp sentDate) {
        this.sentDate = sentDate;
    }

    public User getSender() {
        return this.sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return this.recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public String hasAttachments() {
        return this.hasAttachments;
    }

    public void setHasAttachments(String hasAttachments) {
        this.hasAttachments = hasAttachments;
    }

    public boolean isDraft() {
        return this.isDraft;
    }

    public void setDraft(boolean draft) {
        this.isDraft = draft;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }
}
