package com.alsaineybarry.tickets.repositories;

import com.alsaineybarry.tickets.domain.entities.User;
import com.alsaineybarry.tickets.domain.enums.RoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>    {
    Optional<User> findByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.role IS NULL")
    List<User> findUsersWithNullRole();
    
    @Query("SELECT u FROM User u WHERE u.role.name = :roleName")
    Page<User> findByRole(@Param("roleName") RoleEnum roleName, Pageable pageable);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role.name = :roleName")
    Integer countByRole(@Param("roleName") RoleEnum roleName);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role.name = :roleName AND u.createdAt >= :startDate")
    Integer countByRoleAndCreatedAfter(@Param("roleName") RoleEnum roleName, @Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT u FROM User u WHERE u.role.name = :roleName ORDER BY u.createdAt DESC")
    List<User> findTopPerformingOrganizers(@Param("roleName") RoleEnum roleName, Pageable pageable);
}
