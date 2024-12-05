package org.example.repos;

import org.example.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // CRUD-операции над Property
public interface PropertyRepository extends JpaRepository<Property, Long> {
    Optional<Property> findByName(String name);
}
