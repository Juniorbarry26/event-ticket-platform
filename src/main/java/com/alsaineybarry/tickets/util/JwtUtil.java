package com.alsaineybarry.tickets.util;

import java.util.UUID;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;

public final class JwtUtil {
    private JwtUtil(){
    }

    public static UUID parseUserId(Jwt jwt) {
        if (jwt == null) {
            throw new IllegalArgumentException("JWT token cannot be null");
        }
        String subject = jwt.getSubject();
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("JWT subject cannot be null or empty");
        }
        return UUID.fromString(subject);
    }
    
    public static UUID parseUserId(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalArgumentException("Authentication cannot be null");
        }
        
        // Extract JWT from authentication credentials
        Object credentials = authentication.getCredentials();
        if (credentials instanceof Jwt) {
            return parseUserId((Jwt) credentials);
        } else {
            throw new IllegalArgumentException("Authentication credentials is not a JWT token");
        }
    }
    
    public static UUID getCurrentUserId() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        
        if (authentication == null) {
            throw new IllegalArgumentException("No authentication found in security context");
        }
        
        return parseUserId(authentication);
    }


}