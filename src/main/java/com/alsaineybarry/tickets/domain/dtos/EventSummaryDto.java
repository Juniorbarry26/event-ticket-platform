package com.alsaineybarry.tickets.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSummaryDto {
    private UUID eventId;
    private String eventName;
    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;
    private String venue;
    private String status;
    
    // Revenue metrics
    private BigDecimal totalRevenue;
    private BigDecimal totalPotentialRevenue;
    private BigDecimal revenueLoss;
    
    // Sales metrics
    private Integer totalTicketsSold;
    private Integer totalTicketsAvailable;
    private Double salesPercentage;
    
    // Profitability
    private BigDecimal netProfit;
    private Double profitMargin;
}
