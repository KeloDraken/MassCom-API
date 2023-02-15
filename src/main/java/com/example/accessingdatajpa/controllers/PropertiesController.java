package com.example.accessingdatajpa.controllers;

import com.example.accessingdatajpa.dto.CreateProperty;
import com.example.accessingdatajpa.entities.Property;
import com.example.accessingdatajpa.repositories.PropertyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.example.accessingdatajpa.Utils.parseId;

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
        Property newProperty = new Property(property.propertyName(), property.propertyAddress());
        Property p = propertyRepository.save(newProperty);
        return new ResponseEntity<>(p, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/properties/delete/{propertyId}/")
    public ResponseEntity<Property> deleteProperty(@PathVariable("propertyId") String pathVariable) {
        Optional<Long> propertyId = parseId(pathVariable);

        if (propertyId.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Property> property = propertyRepository.findById(propertyId.get());

        if (property.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        propertyRepository.delete(property.get());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
