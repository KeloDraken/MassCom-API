package com.example.accessingdatajpa.controllers;

import com.example.accessingdatajpa.entities.User;
import com.example.accessingdatajpa.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/users/")
    public ResponseEntity<Iterable<User>> all() {
        Iterable<User> users = repository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{pathVariable}/")
    public ResponseEntity<User> getUserById(@PathVariable("pathVariable") String pathVariable) {
        long userId;
        try {
            userId = Long.parseLong(pathVariable);
        } catch (NumberFormatException numberFormatException) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = repository.findUserById(userId);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
