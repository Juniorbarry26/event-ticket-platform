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
public class EventRevenueDto {
    private UUID eventId;
    private String eventName;
    private LocalDateTime eventStart;
    private String venue;
    
    // Revenue metrics
    private BigDecimal revenue;
    private BigDecimal potentialRevenue;
    private BigDecimal revenueLoss;
    private BigDecimal profit;
    
    // Sales metrics
    private Integer ticketsSold;
    private Integer ticketsAvailable;
    private Double salesPercentage;
}
