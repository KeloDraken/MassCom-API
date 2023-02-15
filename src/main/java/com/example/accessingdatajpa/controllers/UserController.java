package com.example.accessingdatajpa.controllers;

import com.example.accessingdatajpa.entities.User;
import com.example.accessingdatajpa.entities.UserEmail;
import com.example.accessingdatajpa.repositories.UserEmailRepository;
import com.example.accessingdatajpa.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.example.accessingdatajpa.utils.parseId;

@RestController
public class UserController {
    private final UserRepository userRepository;
    private final UserEmailRepository userEmailRepository;

    public UserController(UserRepository userRepository, UserEmailRepository userEmailRepository) {
        this.userRepository = userRepository;
        this.userEmailRepository = userEmailRepository;
    }

    private Optional<User> getUser(long userId) {
        User user = userRepository.findUserById(userId);

        if (user == null) return Optional.empty();

        return Optional.of(user);
    }

    @GetMapping("/users/")
    public ResponseEntity<Iterable<User>> all() {
        Iterable<User> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{pathVariable}/")
    public ResponseEntity<User> getUserById(@PathVariable("pathVariable") String pathVariable) {
        Optional<Long> userId = parseId(pathVariable);

        if (userId.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<User> user = getUser(userId.get());
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/users/{pathVariable}/emails/")
    public ResponseEntity<Iterable<UserEmail>> getUserEmails(@PathVariable("pathVariable") String pathVariable) {
        Optional<Long> userId = parseId(pathVariable);

        if (userId.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<User> user = getUser(userId.get());

        if (user.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Iterable<UserEmail> userEmails = userEmailRepository.findUserEmailByUser(user.get());

        return new ResponseEntity<>(userEmails, HttpStatus.OK);
    }
}
