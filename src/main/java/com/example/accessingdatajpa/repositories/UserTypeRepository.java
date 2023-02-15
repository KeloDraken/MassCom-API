package com.example.accessingdatajpa.repositories;

import com.example.accessingdatajpa.entities.UserType;
import org.springframework.data.repository.CrudRepository;

public interface UserTypeRepository extends CrudRepository<UserType, Long> {
    UserType getUserTypeByUserId(long id);
}
