package org.example.repos;

import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository // CRUD-операции над User
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
}