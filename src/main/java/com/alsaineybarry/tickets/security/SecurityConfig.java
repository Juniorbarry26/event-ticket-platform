package com.alsaineybarry.tickets.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.Customizer.withDefaults;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Allow public access to auth endpoints
                .requestMatchers("/auth/register", "/auth/register/user", "/auth/register/organizer", "/auth/register/admin").permitAll()
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers("/api/v1/published-events/**").permitAll()
                // Allow access to Swagger and API documentation
                .requestMatchers("/v2/api-docs", "/v3/api-docs", "/v3/api-docs/**").permitAll()
                .requestMatchers("/swagger-resources/**", "/configuration/ui", "/configuration/security").permitAll()
                .requestMatchers("/swagger-ui/**", "/webjars/**", "/swagger-ui.html").permitAll()
                // Health check endpoint
                .requestMatchers("/actuator/health").permitAll()
                // Public templates + static resources
                .requestMatchers("/", "/index", "/payments/**", "/css/**", "/js/**", "/images/**").permitAll()
                // Require authentication for protected endpoints
                .requestMatchers("/api/v1/events/**").authenticated()
                .requestMatchers("/api/v1/tickets/**").authenticated()
                .requestMatchers("/api/v1/ticket-types/**").authenticated()
                .requestMatchers("/api/v1/ticket-validations/**").authenticated()
                .requestMatchers("/api/v1/admin/**").authenticated()
                .anyRequest().authenticated()
            )
            .cors(withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}