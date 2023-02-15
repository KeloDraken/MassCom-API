package com.example.accessingdatajpa.entities;

public class UserEmail {

  private long id;
  private long userId;
  private long emailMessageId;
  private String received;
  private java.sql.Timestamp datetimeReceived;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
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


  public java.sql.Timestamp getDatetimeReceived() {
    return datetimeReceived;
  }

  public void setDatetimeReceived(java.sql.Timestamp datetimeReceived) {
    this.datetimeReceived = datetimeReceived;
  }

}
