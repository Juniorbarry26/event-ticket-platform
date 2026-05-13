package com.alsaineybarry.tickets.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketTypeProfitLossDto {
    private UUID ticketTypeId;
    private String ticketTypeName;
    private BigDecimal price;
    private Integer totalAvailable;
    private Integer totalSold;
    private Integer remaining;
    
    // Revenue
    private BigDecimal revenue;
    private BigDecimal potentialRevenue;
    private BigDecimal revenueLoss;
    
    // Profit metrics
    private BigDecimal profit;
    private BigDecimal profitMargin;
    private Double salesPercentage;
}
