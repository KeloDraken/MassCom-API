package com.example.accessingdatajpa.entities;

public class EmailAttachment {

    private long id;
    private long fileTypeId;
    private String fileLocation;
    private long emailMessageId;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getFileTypeId() {
        return fileTypeId;
    }

    public void setFileTypeId(long fileTypeId) {
        this.fileTypeId = fileTypeId;
    }


    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }


    public long getEmailMessageId() {
        return emailMessageId;
    }

    public void setEmailMessageId(long emailMessageId) {
        this.emailMessageId = emailMessageId;
    }

}
