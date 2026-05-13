package com.alsaineybarry.tickets.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminOrganizerStatisticsDto {
    
    private Integer totalOrganizers;
    private Integer activeOrganizers;
    private Integer newOrganizersThisMonth;
    private Integer totalEventsCreated;
    private Integer totalTicketsSold;
    private Double totalRevenue;
    private Double totalProfit;
    private LocalDateTime lastUpdated;
    private List<AdminOrganizerDto> topPerformingOrganizers;
    
}
