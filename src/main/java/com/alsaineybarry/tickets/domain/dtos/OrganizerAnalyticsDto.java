package com.alsaineybarry.tickets.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizerAnalyticsDto {
    private UUID eventId;
    private String eventName;
    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;
    private String venue;
    
    // Revenue metrics
    private BigDecimal totalRevenue;
    private BigDecimal totalPotentialRevenue;
    private BigDecimal revenueLoss;
    private BigDecimal netProfit;
    
    // Sales metrics
    private Integer totalTicketsSold;
    private Integer totalTicketsAvailable;
    private Integer ticketsRemaining;
    private Double salesPercentage;
    
    // Financial metrics
    private BigDecimal averageTicketPrice;
    private BigDecimal highestTicketPrice;
    private BigDecimal lowestTicketPrice;
    
    // List of ticket purchasers
    private List<TicketPurchaserDto> ticketPurchasers;
    
    // Sales by ticket type
    private List<TicketTypeSalesDto> ticketTypeSales;
}
