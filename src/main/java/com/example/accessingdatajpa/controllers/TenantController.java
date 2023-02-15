package com.example.accessingdatajpa.controllers;

import com.example.accessingdatajpa.dto.CreateUser;
import com.example.accessingdatajpa.dto.ResponseUser;
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
import java.util.Optional;

import static com.example.accessingdatajpa.Utils.parseId;

@RestController
public class TenantController {
    private final UserRepository userRepository;
    private final UserEmailRepository userEmailRepository;
    private final PropertyRepository propertyRepository;
    private final UserTypeRepository userTypeRepository;

    public TenantController(UserRepository userRepository,
                            UserEmailRepository userEmailRepository,
                            PropertyRepository propertyRepository,
                            UserTypeRepository userTypeRepository) {
        this.userRepository = userRepository;
        this.userEmailRepository = userEmailRepository;
        this.propertyRepository = propertyRepository;
        this.userTypeRepository = userTypeRepository;
    }

    private Optional<User> getUser(long userId) {
        User user = userRepository.findUserById(userId);

        if (user == null) return Optional.empty();

        return Optional.of(user);
    }

    private Optional<Property> getProperty(long propertyId) {
        Property property = propertyRepository.getPropertiesById(propertyId);

        if (property == null) return Optional.empty();

        return Optional.of(property);
    }

    private User setUpUser(CreateUser userDTO) {
        User user = new User(userDTO.firstname(), userDTO.emailAddress(), userDTO.surname());

        long propertyId = userDTO.propertyId();
        Optional<Property> property = getProperty(propertyId);

        if (property.isEmpty()) throw new RuntimeException(String.format("No such Property with id: %d", propertyId));

        user.setProperty(property.get());

        LocalDateTime now = LocalDateTime.now();
        user.setDateJoined(Timestamp.valueOf(now));

        return userRepository.save(user);
    }

    @GetMapping(value = "/tenants/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<User>> getAllUsers() {
        Iterable<User> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/tenants/{tenantId}/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseUser> getUserById(@PathVariable("tenantId") String pathVariable) {
        Optional<Long> userId = parseId(pathVariable);

        if (userId.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<User> user = getUser(userId.get());
        if (user.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        UserType userType = userTypeRepository.getUserTypeByUserId(user.get().getId());
        ResponseUser responseUser = new ResponseUser(user.get(), userType);

        return new ResponseEntity<>(responseUser, HttpStatus.OK);
    }

    @PostMapping(value = "/tenants/register/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> registerTenant(@RequestBody CreateUser userDTO) {
        User user;
        try {
            user = setUpUser(userDTO);
        } catch (RuntimeException runtimeException) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UserType userType = new UserType(user, "tenant");
        userTypeRepository.save(userType);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/admin/register/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> registerAdminUser(@RequestBody CreateUser userDTO) {
        User user;
        try {
            user = setUpUser(userDTO);
        } catch (RuntimeException runtimeException) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UserType userType = new UserType(user, "admin");
        userTypeRepository.save(userType);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/tenants/{tenantId}/emails/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<UserEmail>> getTenantEmails(@PathVariable("tenantId") String pathVariable) {
        Optional<Long> userId = parseId(pathVariable);

        if (userId.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<User> user = getUser(userId.get());

        if (user.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Iterable<UserEmail> userEmails = userEmailRepository.findUserEmailByUser(user.get());

        return new ResponseEntity<>(userEmails, HttpStatus.OK);
    }
}
