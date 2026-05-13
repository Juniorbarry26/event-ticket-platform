package com.alsaineybarry.tickets.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizerDashboardDto {
    private UUID organizerId;
    private String organizerName;
    private String organizerEmail;
    
    // Overall metrics
    private Integer totalEvents;
    private Integer activeEvents;
    private Integer pastEvents;
    private Integer upcomingEvents;
    
    // Overall revenue
    private BigDecimal totalRevenueAllEvents;
    private BigDecimal totalPotentialRevenueAllEvents;
    private BigDecimal totalRevenueLossAllEvents;
    private BigDecimal totalNetProfitAllEvents;
    
    // Overall ticket sales
    private Integer totalTicketsSoldAllEvents;
    private Integer totalTicketsAvailableAllEvents;
    private Double overallSalesPercentage;
    
    // Recent activity
    private List<RecentActivityDto> recentActivities;
    
    // Event summaries
    private List<EventSummaryDto> eventSummaries;
}
