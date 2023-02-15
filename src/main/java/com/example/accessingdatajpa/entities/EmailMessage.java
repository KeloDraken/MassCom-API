package com.example.accessingdatajpa.entities;



public class EmailMessage {

  private long id;
  private String body;
  private String emailSubject;
  private java.sql.Timestamp sentDate;
  private long senderId;
  private long recipientId;
  private String hasAttachments;
  private String isDraft;


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


  public long getSenderId() {
    return senderId;
  }

  public void setSenderId(long senderId) {
    this.senderId = senderId;
  }


  public long getRecipientId() {
    return recipientId;
  }

  public void setRecipientId(long recipientId) {
    this.recipientId = recipientId;
  }


  public String getHasAttachments() {
    return hasAttachments;
  }

  public void setHasAttachments(String hasAttachments) {
    this.hasAttachments = hasAttachments;
  }


  public String getIsDraft() {
    return isDraft;
  }

  public void setIsDraft(String isDraft) {
    this.isDraft = isDraft;
  }

}
