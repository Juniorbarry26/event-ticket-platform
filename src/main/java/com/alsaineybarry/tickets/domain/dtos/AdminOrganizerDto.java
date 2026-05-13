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
public class AdminOrganizerDto {
    
    private UUID organizerId;
    private String organizerName;
    private String organizerEmail;
    private LocalDateTime createdAt;
    private Integer totalEventsCreated;
    private Integer totalTicketsSold;
    private Double totalRevenue;
    private Double totalProfit;
    private Boolean isActive;
    
}
