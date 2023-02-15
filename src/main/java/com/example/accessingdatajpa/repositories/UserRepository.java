package com.example.accessingdatajpa.repositories;


import com.example.accessingdatajpa.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findUserById(long id);
}
