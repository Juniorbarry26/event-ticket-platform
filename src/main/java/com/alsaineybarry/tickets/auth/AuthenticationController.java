package com.alsaineybarry.tickets.auth;

import com.alsaineybarry.tickets.auth.AuthenticationRequest;
import com.alsaineybarry.tickets.auth.AuthenticationResponse;
import com.alsaineybarry.tickets.auth.RegistrationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register/admin")
    @Operation(summary = "Register a new admin", description = "Registers a new admin and returns JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Admin registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<AuthenticationResponse> registerAdmin(@Valid @RequestBody RegistrationRequest request) {
        AuthenticationResponse response = authenticationService.register(request, "ADMIN");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/register/organizer")
    @Operation(summary = "Register a new organizer", description = "Registers a new organizer and returns JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Organizer registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<AuthenticationResponse> registerOrganizer(@Valid @RequestBody RegistrationRequest request) {
        AuthenticationResponse response = authenticationService.register(request, "ORGANIZER");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/register/user")
    @Operation(summary = "Register a new user", description = "Registers a new user and returns JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<AuthenticationResponse> registerUser(@Valid @RequestBody RegistrationRequest request) {
        AuthenticationResponse response = authenticationService.register(request, "USER");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user and returns JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegistrationRequest request) {
        AuthenticationResponse response = authenticationService.register(request, "USER");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Authenticates a user and returns JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }
}
