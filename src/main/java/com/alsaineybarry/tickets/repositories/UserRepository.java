package com.alsaineybarry.tickets.repositories;

import com.alsaineybarry.tickets.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>    {
}
