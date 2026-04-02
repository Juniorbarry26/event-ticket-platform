package com.alsaineybarry.tickets.config;

import com.alsaineybarry.tickets.domain.entities.Role;
import com.alsaineybarry.tickets.domain.enums.RoleEnum;
import com.alsaineybarry.tickets.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(2) // Run after DatabaseMigration
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        initializeRoles();
    }

    private void initializeRoles() {
        if (roleRepository.count() == 0) {
            log.info("Initializing default roles...");
            
            Arrays.stream(RoleEnum.values())
                    .forEach(roleEnum -> {
                        Role role = Role.builder()
                                .name(roleEnum)
                                .description(getRoleDescription(roleEnum))
                                .build();
                        roleRepository.save(role);
                        log.info("Created role: {}", roleEnum.getRoleName());
                    });
            
            log.info("Default roles initialization completed.");
        } else {
            log.info("Roles already exist in the database.");
        }
    }

    private String getRoleDescription(RoleEnum roleEnum) {
        return switch (roleEnum) {
            case USER -> "Regular user who can attend events";
            case ORGANIZER -> "Event organizer who can create and manage events";
            case ADMIN -> "System administrator with full access";
        };
    }
}
