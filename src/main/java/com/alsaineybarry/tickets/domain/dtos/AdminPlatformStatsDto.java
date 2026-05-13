package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AdminPlatformStatsDto {
    private Integer totalUsers;
    private Integer totalOrganizers;
    private Integer totalEvents;
    private Integer totalTickets;
    private Integer activeEvents;
    private Integer completedEvents;
    private BigDecimal totalRevenue;
    private BigDecimal monthlyRevenue;
    private Integer newUsersThisMonth;
    private Integer newOrganizersThisMonth;
    private Integer newEventsThisMonth;
    private LocalDateTime lastUpdated;
}
