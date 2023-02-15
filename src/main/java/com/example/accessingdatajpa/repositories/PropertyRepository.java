package com.example.accessingdatajpa.repositories;

import com.example.accessingdatajpa.entities.Property;
import org.springframework.data.repository.CrudRepository;

public interface PropertyRepository extends CrudRepository<Property, Long> {
    Property getPropertiesById(long id);
}
