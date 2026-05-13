package com.alsaineybarry.tickets.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizerProfitSummaryDto {
    
    private UUID eventId;
    private String eventName;
    private LocalDateTime eventStartDate;
    private LocalDateTime eventEndDate;
    private Integer totalTicketsSold;
    private Double totalRevenue;
    private Double totalProfit;
    private List<TicketTypeProfitDto> ticketTypeProfits;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TicketTypeProfitDto {
        private UUID ticketTypeId;
        private String ticketTypeName;
        private Double ticketPrice;
        private Integer ticketsSold;
        private Double revenue;
        private Double profit;
    }
}
