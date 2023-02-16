package com.example.accessingdatajpa.repositories;

import com.example.accessingdatajpa.entities.EmailMessage;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;

public interface EmailRepository extends CrudRepository<EmailMessage, Long> {
    @Procedure(value = "uspSendEmail", procedureName = "uspSendEmail")
    void uspSendEmail(Long senderId, Long recipientId, String subject, String body);
}
