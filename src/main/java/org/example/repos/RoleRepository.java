package org.example.repos;

import org.example.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // CRUD-операции над Role
public interface RoleRepository extends JpaRepository<Role, Long> {

}
