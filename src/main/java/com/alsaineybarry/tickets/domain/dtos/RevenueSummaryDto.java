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
public class RevenueSummaryDto {
    private UUID organizerId;
    private String organizerName;
    
    // Revenue metrics
    private BigDecimal totalRevenue;
    private BigDecimal totalPotentialRevenue;
    private BigDecimal revenueLoss;
    private BigDecimal netProfit;
    
    // Revenue by event
    private List<EventRevenueDto> eventRevenues;
    
    // Revenue trends (if needed for future enhancement)
    private BigDecimal averageRevenuePerEvent;
    private BigDecimal highestRevenueEvent;
    private BigDecimal lowestRevenueEvent;
}
