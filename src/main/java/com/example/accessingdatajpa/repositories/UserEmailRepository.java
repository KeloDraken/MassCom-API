package com.example.accessingdatajpa.repositories;

import com.example.accessingdatajpa.entities.User;
import com.example.accessingdatajpa.entities.UserEmail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEmailRepository extends JpaRepository<UserEmail, Long> {
    Iterable<UserEmail> findUserEmailByUser(User user);
    UserEmail findUserEmailsByUser(User user);
}
