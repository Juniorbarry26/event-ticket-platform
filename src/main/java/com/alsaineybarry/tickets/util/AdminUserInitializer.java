package com.alsaineybarry.tickets.util;

import com.alsaineybarry.tickets.domain.entities.Role;
import com.alsaineybarry.tickets.domain.entities.User;
import com.alsaineybarry.tickets.domain.enums.RoleEnum;
import com.alsaineybarry.tickets.repositories.RoleRepository;
import com.alsaineybarry.tickets.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createAdminUser();
    }

    private void createAdminUser() {
        String adminEmail = "alsaineyb4@gmail.com";
        String adminPassword = "Avond778@";

        log.info("Starting admin user initialization...");

        // Check if admin user already exists
        if (userRepository.findByEmail(adminEmail).isPresent()) {
            log.info("Admin user already exists: {}", adminEmail);
            return;
        }

        // Ensure all required roles exist
        createRoleIfNotExists(RoleEnum.USER, "Regular user role");
        createRoleIfNotExists(RoleEnum.ORGANIZER, "Event organizer role");
        createRoleIfNotExists(RoleEnum.ADMIN, "Administrator role with full access");

        // Get or create ADMIN role
        Role adminRole = roleRepository.findByName(RoleEnum.ADMIN)
                .orElseThrow(() -> new RuntimeException("Failed to create ADMIN role"));

        // Create admin user
        User adminUser = User.builder()
                .id(UUID.randomUUID())
                .name("Admin User")
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .role(adminRole)
                .build();

        userRepository.save(adminUser);
        log.info("Admin user created successfully: {}", adminEmail);
        log.info("Admin password: {}", adminPassword);
    }

    private void createRoleIfNotExists(RoleEnum roleEnum, String description) {
        roleRepository.findByName(roleEnum).orElseGet(() -> {
            Role role = Role.builder()
                    .id(UUID.randomUUID())
                    .name(roleEnum)
                    .description(description)
                    .build();
            Role savedRole = roleRepository.save(role);
            log.info("Created role: {} with ID: {}", roleEnum, savedRole.getId());
            return savedRole;
        });
    }
}
