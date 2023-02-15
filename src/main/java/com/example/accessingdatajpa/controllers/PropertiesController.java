package com.example.accessingdatajpa.controllers;

import com.example.accessingdatajpa.dto.CreateProperty;
import com.example.accessingdatajpa.entities.Property;
import com.example.accessingdatajpa.repositories.PropertyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PropertiesController {
    private final PropertyRepository propertyRepository;

    public PropertiesController(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @GetMapping(value = "/properties/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Property>> getAllProperties() {
        Iterable<Property> properties = propertyRepository.findAll();
        return new ResponseEntity<>(properties, HttpStatus.OK);
    }

    @PostMapping(value = "/properties/create/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Property> createNewProperty(@RequestBody CreateProperty property) {
        Property newProperty = new Property(property.getPropertyName(), property.getPropertyAddress());
        Property p = propertyRepository.save(newProperty);
        return new ResponseEntity<>(p, HttpStatus.CREATED);
    }
}
