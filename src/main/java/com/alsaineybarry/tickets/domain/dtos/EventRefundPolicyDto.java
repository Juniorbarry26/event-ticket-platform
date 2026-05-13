package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class EventRefundPolicyDto {
    private java.util.UUID eventId;
    private String refundPolicy;
    private Integer refundWindowDays;
    private Integer partialRefundWindowDays;
    private BigDecimal refundFeePercentage;
    private BigDecimal processingFee;
    private String currency;
    private Boolean automaticRefund;
    private String refundMethod;
}
