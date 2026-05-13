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
public class ProfitLossAnalysisDto {
    private UUID eventId;
    private String eventName;
    
    // Revenue
    private BigDecimal totalRevenue;
    private BigDecimal ticketRevenue;
    private BigDecimal otherRevenue;
    
    // Costs (for future enhancement - currently we'll calculate based on revenue loss)
    private BigDecimal totalCosts;
    private BigDecimal platformFees;
    private BigDecimal processingFees;
    private BigDecimal otherCosts;
    
    // Profit/Loss
    private BigDecimal grossProfit;
    private BigDecimal netProfit;
    private BigDecimal profitMargin;
    
    // Loss analysis
    private BigDecimal totalLoss;
    private BigDecimal unsoldTicketsLoss;
    private BigDecimal otherLosses;
    
    // Breakdown by ticket type
    private List<TicketTypeProfitLossDto> ticketTypeBreakdown;
    
    // Metrics
    private Double breakEvenPercentage;
    private Integer ticketsNeededToBreakEven;
}
