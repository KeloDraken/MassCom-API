package com.example.accessingdatajpa.controllers;

import com.example.accessingdatajpa.dto.PropertyDTO;
import com.example.accessingdatajpa.dto.ResponsePropertyDTO;
import com.example.accessingdatajpa.entities.Property;
import com.example.accessingdatajpa.repositories.PropertyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RestController
public class PropertiesController {
    private final PropertyRepository propertyRepository;

    public PropertiesController(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    private static void updatePropertyValues(PropertyDTO propertyDTO, Property property) {
        if (propertyDTO.propertyName() != null && !propertyDTO.propertyName().isEmpty() && !propertyDTO.propertyName().isBlank()) {
            property.setPropertyName(propertyDTO.propertyName());
        }
        if (propertyDTO.propertyAddress() != null && !propertyDTO.propertyAddress().isEmpty() && !propertyDTO.propertyAddress().isBlank()) {
            property.setPropertyAddress(propertyDTO.propertyAddress());
        }
    }

    @GetMapping(value = "/properties/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<ResponsePropertyDTO>> getAllProperties() {
        Iterable<Property> properties = this.propertyRepository.findAll();

        List<ResponsePropertyDTO> responseProperties = StreamSupport.stream(properties.spliterator(), true)
                .filter(property -> !property.isDeleted())
                .map(property -> new ResponsePropertyDTO(
                        property.getId(),
                        property.getPropertyName(),
                        property.getPropertyAddress()
                ))
                .toList();

        return new ResponseEntity<>(responseProperties, HttpStatus.OK);
    }

    @GetMapping(value = "/properties/{propertyId}/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getPropertyById(@PathVariable("propertyId") Long propertyId) {
        Optional<Property> property = this.propertyRepository.findById(propertyId);

        if (property.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("No such property with id: %d", propertyId));
        }

        return new ResponseEntity<>(property, HttpStatus.OK);
    }

    @PostMapping(value = "/properties/create/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponsePropertyDTO> createNewProperty(@RequestBody PropertyDTO property) {
        Property newProperty = new Property(property.propertyName(), property.propertyAddress());

        Property p = this.propertyRepository.save(newProperty);
        ResponsePropertyDTO response = new ResponsePropertyDTO(p.getId(), p.getPropertyName(), p.getPropertyAddress());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/properties/delete/{propertyId}/")
    public ResponseEntity<Object> deleteProperty(@PathVariable("propertyId") Long pathVariable) {
        Optional<Property> property = this.propertyRepository.findById(pathVariable);

        if (property.isEmpty() || property.get().isDeleted()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("The property with id %s does not exist", pathVariable));
        }

        Property p = property.get();
        p.setDeleted(true);
        this.propertyRepository.save(p);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Property successfully deleted");
    }

    @PatchMapping(value = "/properties/edit/{propertyId}/")
    public ResponseEntity<Object> editProperty(@RequestBody PropertyDTO propertyDTO, @PathVariable("propertyId") Long pathVariable) {
        Optional<Property> property = this.propertyRepository.findById(pathVariable);

        if (property.isEmpty() || property.get().isDeleted()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(String.format("The property with id %s does not exist", pathVariable));
        }

        Property p = property.get();
        PropertiesController.updatePropertyValues(propertyDTO, p);
        this.propertyRepository.save(p);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Property successfully updated");
    }
}
