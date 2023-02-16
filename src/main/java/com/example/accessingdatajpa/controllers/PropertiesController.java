package com.example.accessingdatajpa.controllers;

import com.example.accessingdatajpa.dto.CreateProperty;
import com.example.accessingdatajpa.dto.ResponseProperty;
import com.example.accessingdatajpa.entities.Property;
import com.example.accessingdatajpa.repositories.PropertyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.example.accessingdatajpa.Utils.parseId;

@RestController
public class PropertiesController {
    private final PropertyRepository propertyRepository;

    public PropertiesController(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @GetMapping(value = "/properties/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<ResponseProperty>> getAllProperties() {
        Iterable<Property> properties = propertyRepository.findAll();

        List<ResponseProperty> responseProperties = StreamSupport.stream(properties.spliterator(), true)
                .filter(property -> !property.isDeleted())
                .map(property -> new ResponseProperty(property.getId(), property.getPropertName(), property.getPropertyAddress()))
                .toList();

        return new ResponseEntity<>(responseProperties, HttpStatus.OK);
    }

    @PostMapping(value = "/properties/create/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseProperty> createNewProperty(@RequestBody CreateProperty property) {
        Property newProperty = new Property(property.propertyName(), property.propertyAddress());
        Property p = propertyRepository.save(newProperty);
        ResponseProperty response = new ResponseProperty(p.getId(), p.getPropertName(), p.getPropertyAddress());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/properties/delete/{propertyId}/")
    public ResponseEntity<Object> deleteProperty(@PathVariable("propertyId") String pathVariable) {
        Optional<Long> propertyId = parseId(pathVariable);

        if (propertyId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(String.format("There is no such property with id %s", pathVariable));
        }

        Optional<Property> property = propertyRepository.findById(propertyId.get());

        if (property.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("The property with id %s does not exist", pathVariable));
        }

        Property p = property.get();

        if (p.isDeleted()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("The property with id %s does not exist", pathVariable));
        }

        p.setDeleted(true);
        propertyRepository.save(p);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
