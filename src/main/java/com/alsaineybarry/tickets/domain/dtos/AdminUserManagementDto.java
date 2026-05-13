package com.alsaineybarry.tickets.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserManagementDto {
    
    private UUID userId;
    private String name;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private Boolean isActive;
    private Boolean isBlocked;
    private Integer eventsAttended;
    private Integer ticketsPurchased;
    
}
