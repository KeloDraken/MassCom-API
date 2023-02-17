package com.example.accessingdatajpa.controllers;

import com.example.accessingdatajpa.dto.EmailMessageDTO;
import com.example.accessingdatajpa.entities.Property;
import com.example.accessingdatajpa.entities.User;
import com.example.accessingdatajpa.entities.UserType;
import com.example.accessingdatajpa.repositories.EmailRepository;
import com.example.accessingdatajpa.repositories.PropertyRepository;
import com.example.accessingdatajpa.repositories.UserRepository;
import com.example.accessingdatajpa.repositories.UserTypeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RestController
public class EmailController {
    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final UserTypeRepository userTypeRepository;
    private final PropertyRepository propertyRepository;

    public EmailController(
            UserRepository userRepository,
            EmailRepository emailRepository,
            UserTypeRepository userTypeRepository,
            PropertyRepository propertyRepository) {
        this.userRepository = userRepository;
        this.emailRepository = emailRepository;
        this.userTypeRepository = userTypeRepository;
        this.propertyRepository = propertyRepository;
    }

    @PostMapping(value = "/emails/send/")
    public ResponseEntity<Object> sendEmails(EmailMessageDTO emailMessageDTO) {
        ResponseEntity<Object> dtoErrors = this.validateUsersBeforeSend(emailMessageDTO);
        if (dtoErrors != null) return dtoErrors;

        StreamSupport.stream(userRepository.findAll().spliterator(), true)
                .filter(user -> !user.isDeleted())
                .map(user -> {
                    emailRepository.uspSendEmail(
                            emailMessageDTO.from(),
                            emailMessageDTO.to(),
                            emailMessageDTO.subject(),
                            emailMessageDTO.body()
                    );
                    return user;
                });

        return ResponseEntity.status(HttpStatus.CREATED).body("Emails have been successfully sent to all tenants");
    }

    private ResponseEntity<Object> validateUsersBeforeSend(EmailMessageDTO emailMessageDTO) {
        User admin = this.userRepository.findUserById(emailMessageDTO.from());
        if (admin.isDeleted()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("No such user with id %d", admin.getId()));
        }

        UserType userType = this.userTypeRepository.getUserTypeByUserId(emailMessageDTO.from());
        boolean checkAdmin = !userType.getUserType().equalsIgnoreCase("admin") ||
                !Objects.equals(emailMessageDTO.property(), admin.getProperty().getId());

        if (checkAdmin) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You don't have the right privileges to perform this action");
        }

        User tenant = this.userRepository.findUserById(emailMessageDTO.to());
        if (tenant.isDeleted()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("No such user with id %d", admin.getId()));
        }

        Optional<Property> property = this.propertyRepository.findById(emailMessageDTO.property());
        if (property.isEmpty() || property.get().isDeleted()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("There is no such property with id %s", emailMessageDTO.property()));
        }

        return null;
    }
}
