package com.alsaineybarry.tickets.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizerOverallProfitDto {
    
    private Integer totalEvents;
    private Integer totalTicketsSold;
    private Double totalRevenue;
    private Double totalProfit;
    private List<OrganizerProfitSummaryDto> eventProfits;
    
}
