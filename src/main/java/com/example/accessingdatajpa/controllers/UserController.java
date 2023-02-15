package com.example.accessingdatajpa.controllers;

import com.example.accessingdatajpa.entities.Property;
import com.example.accessingdatajpa.entities.User;
import com.example.accessingdatajpa.entities.UserEmail;
import com.example.accessingdatajpa.repositories.PropertyRepository;
import com.example.accessingdatajpa.repositories.UserEmailRepository;
import com.example.accessingdatajpa.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.accessingdatajpa.utils.parseId;


@RestController
public class UserController {
    private final UserRepository userRepository;
    private final UserEmailRepository userEmailRepository;
    private final PropertyRepository propertyRepository;

    public UserController(UserRepository userRepository, UserEmailRepository userEmailRepository, PropertyRepository propertyRepository) {
        this.userRepository = userRepository;
        this.userEmailRepository = userEmailRepository;
        this.propertyRepository = propertyRepository;
    }

    private Optional<User> getUser(long userId) {
        User user = userRepository.findUserById(userId);

        if (user == null) return Optional.empty();

        return Optional.of(user);
    }

    @GetMapping(value = "/users/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<User>> getAllUsers() {
        Iterable<User> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/users/{userId}/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserById(@PathVariable("userId") String pathVariable) {
        Optional<Long> userId = parseId(pathVariable);

        if (userId.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<User> user = getUser(userId.get());
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/users/register/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createNewUser(@RequestBody User user) {
        Property property = propertyRepository.getPropertiesById(user.getProperty().getId());
        LocalDateTime now = LocalDateTime.now();
        user.setDateJoined(Timestamp.valueOf(now));

        user.setProperty(property);
        userRepository.save(user);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/users/{userId}/emails/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<UserEmail>> getUserEmails(@PathVariable("userId") String pathVariable) {
        Optional<Long> userId = parseId(pathVariable);

        if (userId.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<User> user = getUser(userId.get());

        if (user.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Iterable<UserEmail> userEmails = userEmailRepository.findUserEmailByUser(user.get());

        return new ResponseEntity<>(userEmails, HttpStatus.OK);
    }
}
