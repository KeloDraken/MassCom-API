package com.example.accessingdatajpa.repositories;

import com.example.accessingdatajpa.entities.EmailMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailMessageRepository extends JpaRepository<EmailMessage, Integer> {
    @Procedure(name = "uspSendEmail")
    void sendEmail(long senderId, long recipientId, String subject, String body);
}
