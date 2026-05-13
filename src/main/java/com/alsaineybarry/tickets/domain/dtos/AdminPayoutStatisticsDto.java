package com.alsaineybarry.tickets.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminPayoutStatisticsDto {
    private Integer totalPayouts;
    private Integer pendingPayouts;
    private Integer processingPayouts;
    private Integer completedPayouts;
    private Integer failedPayouts;
    private BigDecimal totalAmount;
    private BigDecimal pendingAmount;
    private BigDecimal completedAmount;
    private BigDecimal failedAmount;
}
