package com.alsaineybarry.tickets.repositories;

import com.alsaineybarry.tickets.domain.entities.Role;
import com.alsaineybarry.tickets.domain.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(RoleEnum name);
    boolean existsByName(RoleEnum name);
}
