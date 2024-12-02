package org.example.service;

import org.example.model.Property;
import org.example.repos.PropertyRepository;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {
    private final PropertyRepository propertyRepository;
    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }
    public void addProperty(String name, String description,
            double cost, double latitude, double longitude) {
        if(propertyRepository.findByName(name).isPresent()) {
            return;
        }
        Property property = new Property();
        property.setName(name);
        property.setDescription(description);
        property.setCost(cost);
        property.setLatitude(latitude);
        property.setLongitude(longitude);
        propertyRepository.save(property);
    }
}
