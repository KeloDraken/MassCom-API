package com.example.accessingdatajpa.controllers;

import com.example.accessingdatajpa.dto.EmailMessageDTO;
import com.example.accessingdatajpa.entities.*;
import com.example.accessingdatajpa.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

@RestController
public class EmailController {
    private final UserRepository userRepository;
    private final EmailMessageRepository emailRepository;
    private final UserTypeRepository userTypeRepository;
    private final PropertyRepository propertyRepository;
    private final UserEmailRepository userEmailRepository;

    public EmailController(
            UserRepository userRepository,
            EmailMessageRepository emailRepository,
            UserTypeRepository userTypeRepository,
            PropertyRepository propertyRepository,
            UserEmailRepository userEmailRepository) {
        this.userRepository = userRepository;
        this.emailRepository = emailRepository;
        this.userTypeRepository = userTypeRepository;
        this.propertyRepository = propertyRepository;
        this.userEmailRepository = userEmailRepository;
    }

    @PostMapping(value = "/emails/send/")
    public ResponseEntity<Object> sendEmails(EmailMessageDTO emailMessageDTO) {
        ResponseEntity<Object> dtoErrors = this.validateUsersBeforeSend(emailMessageDTO);
        if (dtoErrors != null) return dtoErrors;

        for (User user : this.userRepository.findAll()) {
            if (!user.isDeleted() && !user.getId().equals(emailMessageDTO.from())) {
                this.sendMessage(emailMessageDTO, user);
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Emails have been successfully sent to all tenants");
    }

    private void sendMessage(EmailMessageDTO emailMessageDTO, User user) {
        Optional<User> sender = this.getUser(emailMessageDTO.from());

        if (sender.isEmpty()) {
            throw new RuntimeException(String.format("No such user with id: %d", emailMessageDTO.from()));
        }

        EmailMessage sentMessage = this.emailRepository.save(
                new EmailMessage(sender.get(), user, emailMessageDTO.subject(), emailMessageDTO.body())
        );
        UserEmail userEmail = new UserEmail(user, sentMessage);
        this.userEmailRepository.save(userEmail);
        System.out.println("here");
    }

    private ResponseEntity<Object> validateUsersBeforeSend(EmailMessageDTO emailMessageDTO) {
        User admin = this.userRepository.findUserById(emailMessageDTO.from());
        if (admin == null || admin.isDeleted()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("No such user with id %d", emailMessageDTO.from()));
        }

        UserType userType = this.userTypeRepository.getUserTypeByUserId(emailMessageDTO.from());
        boolean checkAdmin = !userType.getUserType().equalsIgnoreCase("admin") ||
                !Objects.equals(emailMessageDTO.property(), admin.getProperty().getId());

        if (checkAdmin) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You don't have the right privileges to perform this action");
        }

        Optional<Property> property = this.propertyRepository.findById(emailMessageDTO.property());
        if (property.isEmpty() || property.get().isDeleted()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("There is no such property with id %s", emailMessageDTO.property()));
        }

        return null;
    }

    private Optional<User> getUser(long userId) {
        User user = userRepository.findUserById(userId);

        if (user == null) {
            return Optional.empty();
        }

        return Optional.of(user);
    }
}
