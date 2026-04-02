package com.alsaineybarry.tickets.auth;

import com.alsaineybarry.tickets.domain.entities.Role;
import com.alsaineybarry.tickets.domain.entities.User;
import com.alsaineybarry.tickets.domain.enums.RoleEnum;
import com.alsaineybarry.tickets.repositories.RoleRepository;
import com.alsaineybarry.tickets.repositories.UserRepository;
import com.alsaineybarry.tickets.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse register(RegistrationRequest request, String roleString) {
        RoleEnum roleEnum = RoleEnum.fromString(roleString);
        Role role = roleRepository.findByName(roleEnum)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleString));

        var user = User.builder()
                .id(UUID.randomUUID())
                .name(request.getFirstName() + " " + request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();
        
        userRepository.save(user);
        
        var jwtToken = jwtService.generateToken(user);
        
        var userAuthResponse = UserAuthResponse.builder()
                .id(user.getId())
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .lastModifiedDate(user.getUpdatedAt())
                .build();
        
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(userAuthResponse)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        
        var jwtToken = jwtService.generateToken(user);
        
        String[] nameParts = user.getName().split(" ", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : "";
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        
        var userAuthResponse = UserAuthResponse.builder()
                .id(user.getId())
                .firstname(firstName)
                .lastname(lastName)
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .lastModifiedDate(user.getUpdatedAt())
                .build();
        
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(userAuthResponse)
                .build();
    }
}