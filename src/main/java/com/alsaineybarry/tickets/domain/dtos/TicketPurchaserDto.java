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
public class TicketPurchaserDto {
    private UUID purchaserId;
    private String purchaserName;
    private String purchaserEmail;
    private LocalDateTime purchaseDate;
    private Integer ticketsPurchased;
    private BigDecimal totalAmountPaid;
    private List<String> ticketTypeNames;
    private String status;
}
