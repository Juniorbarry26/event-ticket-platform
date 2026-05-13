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
public class TicketTypeSalesDto {
    private UUID ticketTypeId;
    private String ticketTypeName;
    private String description;
    private BigDecimal price;
    private Integer totalAvailable;
    private Integer totalSold;
    private Integer remaining;
    private BigDecimal revenue;
    private Double salesPercentage;
    private List<TicketPurchaserDto> purchasers;
}
