package org.example.repos;

import org.example.model.Property;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {
    private final PropertyRepository propertyRepository;
    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }
    public void addProperty(String name) {
        if(propertyRepository.findByName(name).isPresent()) {
            return;
        }
        Property property = new Property();
        property.setName(name);
        property.setDescription("");
        property.setLatitude(0.0);
        property.setLongitude(0.0);
        propertyRepository.save(property);
    }
}
