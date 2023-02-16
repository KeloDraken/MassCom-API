package com.example.accessingdatajpa.controllers;

import com.example.accessingdatajpa.dto.CreateUserDTO;
import com.example.accessingdatajpa.dto.ResponseUser;
import com.example.accessingdatajpa.dto.UpdateUserDTO;
import com.example.accessingdatajpa.entities.Property;
import com.example.accessingdatajpa.entities.User;
import com.example.accessingdatajpa.entities.UserEmail;
import com.example.accessingdatajpa.entities.UserType;
import com.example.accessingdatajpa.repositories.PropertyRepository;
import com.example.accessingdatajpa.repositories.UserEmailRepository;
import com.example.accessingdatajpa.repositories.UserRepository;
import com.example.accessingdatajpa.repositories.UserTypeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RestController
public class UserController {
    private final UserRepository userRepository;
    private final UserEmailRepository userEmailRepository;
    private final PropertyRepository propertyRepository;
    private final UserTypeRepository userTypeRepository;

    public UserController(UserRepository userRepository,
                          UserEmailRepository userEmailRepository,
                          PropertyRepository propertyRepository,
                          UserTypeRepository userTypeRepository) {
        this.userRepository = userRepository;
        this.userEmailRepository = userEmailRepository;
        this.propertyRepository = propertyRepository;
        this.userTypeRepository = userTypeRepository;
    }

    private static void updateUserValues(UpdateUserDTO userDTO, User user) {
        if (userDTO.firstname() != null && !userDTO.firstname().isEmpty() && !userDTO.firstname().isBlank()) {
            user.setUserName(userDTO.firstname());
        }
        if (userDTO.surname() != null && !userDTO.surname().isEmpty() && !userDTO.surname().isBlank()) {
            user.setUserSurname(userDTO.surname());
        }
        if (userDTO.emailAddress() != null && !userDTO.emailAddress().isEmpty() && !userDTO.emailAddress().isBlank()) {
            user.setEmailAddress(userDTO.emailAddress());
        }
    }

    @PostMapping(value = "/admin/register/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registerAdminUser(@RequestBody CreateUserDTO userDTO) {
        User user;

        try {
            user = this.setUpUser(userDTO);
        } catch (RuntimeException runtimeException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("No such Property with id: %d", userDTO.propertyId()));
        }

        if (user == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("This email already has an account associated with it");
        }

        UserType userType = new UserType(user, "admin");
        this.userTypeRepository.save(userType);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/tenants/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<User>> getAllUsers() {
        Iterable<User> users = this.userRepository.findAll();

        List<User> responseUsers = StreamSupport.stream(users.spliterator(), false)
                .filter(user -> !user.isDeleted())
                .filter(user -> !user.getProperty().isDeleted())
                .toList();

        return new ResponseEntity<>(responseUsers, HttpStatus.OK);
    }

    @PostMapping(value = "/tenants/register/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registerTenant(@RequestBody CreateUserDTO userDTO) {
        User user;

        try {
            user = setUpUser(userDTO);
        } catch (RuntimeException runtimeException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("No such Property with id: %d", userDTO.propertyId()));
        }

        if (user == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("This email already has an account associated with it");
        }

        UserType userType = new UserType(user, "tenant");
        this.userTypeRepository.save(userType);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/tenants/{tenantId}/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUserById(@PathVariable("tenantId") Long pathVariable) {
        Optional<User> user = this.getUser(pathVariable);

        if (user.isEmpty() || user.get().isDeleted()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("No such user with id: %d", pathVariable));
        }

        UserType userType = this.userTypeRepository.getUserTypeByUserId(user.get().getId());
        ResponseUser responseUser = new ResponseUser(user.get(), userType);

        return new ResponseEntity<>(responseUser, HttpStatus.OK);
    }

    @GetMapping(value = "/tenants/{tenantId}/emails/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getTenantEmails(@PathVariable("tenantId") Long pathVariable) {
        Optional<User> user = this.getUser(pathVariable);

        if (user.isEmpty() || user.get().isDeleted()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("No such user with id: %d", pathVariable));
        }

        Iterable<UserEmail> userEmails = this.userEmailRepository.findUserEmailByUser(user.get());

        return new ResponseEntity<>(userEmails, HttpStatus.OK);
    }

    @PatchMapping(value = "/tenants/edit/{tenantId}/")
    public ResponseEntity<Object> editTenant(@PathVariable("tenantId") Long tenantId, UpdateUserDTO userDTO) {
        User user = this.userRepository.findUserById(tenantId);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("No such user with id: %d", tenantId));
        }

        UserController.updateUserValues(userDTO, user);
        User updatedUser = this.userRepository.save(user);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping(value = "/tenants/delete/{tenantId}/")
    public ResponseEntity<Object> deleteTenant(@PathVariable("tenantId") Long tenantId) {
        User user = this.userRepository.findUserById(tenantId);

        if (user == null || user.isDeleted()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("No such user with id: %d", tenantId));
        }

        user.setDeleted(true);
        this.userRepository.save(user);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User successfully deleted");
    }

    private Optional<User> getUser(long userId) {
        User user = userRepository.findUserById(userId);

        if (user == null) {
            return Optional.empty();
        }

        return Optional.of(user);
    }

    private Optional<Property> getProperty(long propertyId) {
        Property property = this.propertyRepository.getPropertiesById(propertyId);

        if (property == null) {
            return Optional.empty();
        }

        return Optional.of(property);
    }

    private User setUpUser(CreateUserDTO userDTO) {
        User u = this.userRepository.findUsersByEmailAddress(userDTO.emailAddress().toLowerCase());

        if (u != null) return null;

        User user = new User(userDTO.firstname(), userDTO.surname(), userDTO.emailAddress().toLowerCase());

        long propertyId = userDTO.propertyId();
        Optional<Property> property = this.getProperty(propertyId);

        if (property.isEmpty()) {
            throw new RuntimeException(String.format("No such Property with id: %d", propertyId));
        }

        user.setProperty(property.get());

        LocalDateTime now = LocalDateTime.now();
        user.setDateJoined(Timestamp.valueOf(now));

        return this.userRepository.save(user);
    }
}
