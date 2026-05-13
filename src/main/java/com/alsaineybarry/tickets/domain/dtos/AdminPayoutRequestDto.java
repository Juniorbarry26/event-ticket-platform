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
public class AdminPayoutRequestDto {
    private UUID organizerId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String notes;
}
