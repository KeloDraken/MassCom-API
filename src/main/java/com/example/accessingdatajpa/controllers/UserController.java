package com.example.accessingdatajpa.controllers;

import com.example.accessingdatajpa.entities.User;
import com.example.accessingdatajpa.repository.UserRepository;
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
    public Iterable<User> all() {
        return repository.findAll();
    }

    @GetMapping("/users/{pathVariable}/")
    public User getUserById(@PathVariable("pathVariable") String pathVariable) {
        long userId;
        try {
            userId = Long.parseLong(pathVariable);
        } catch (NumberFormatException numberFormatException) {
            throw new RuntimeException("Invalid user id");
        }

        User user = repository.findUserById(userId);

        if (user == null) {
            throw new RuntimeException("User with specified id doesn't exist");
        }
        return user;
    }


}
