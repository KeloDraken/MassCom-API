package com.example.accessingdatajpa.entities;

import jakarta.persistence.*;

@Entity
public class EmailAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JoinColumn(name = "fileTypeId")
    private AllowedAttachmentFileTypes fileType;
    private String fileLocation;
    @OneToOne
    @JoinColumn(name = "emailMessage")
    private EmailMessage emailMessage;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AllowedAttachmentFileTypes getFileTypeId() {
        return this.fileType;
    }

    public void setFileType(AllowedAttachmentFileTypes fileType) {
        this.fileType = fileType;
    }

    public String getFileLocation() {
        return this.fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public EmailMessage getEmailMessage() {
        return this.emailMessage;
    }

    public void setEmailMessage(EmailMessage emailMessage) {
        this.emailMessage = emailMessage;
    }

}
