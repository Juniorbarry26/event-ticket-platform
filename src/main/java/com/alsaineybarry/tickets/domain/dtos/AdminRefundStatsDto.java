package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class AdminRefundStatsDto {
    private Integer totalRefunds;
    private Integer pendingRefunds;
    private Integer approvedRefunds;
    private Integer rejectedRefunds;
    private BigDecimal totalRefundAmount;
    private BigDecimal pendingAmount;
    private BigDecimal approvedAmount;
    private BigDecimal rejectedAmount;
    private Double refundRate;
    private String currency;
}
