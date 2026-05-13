package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class AdminFeeSettingsDto {
    private BigDecimal platformFeePercentage;
    private BigDecimal paymentProcessingFeePercentage;
    private BigDecimal fixedPlatformFee;
    private BigDecimal fixedProcessingFee;
    private BigDecimal minimumFee;
    private BigDecimal maximumFee;
    private String currency;
    private Boolean enableDynamicPricing;
    private BigDecimal taxRate;
    private Boolean taxIncluded;
    private String paymentGateway;
    private Boolean enableRefunds;
    private Integer refundWindowDays;
    private BigDecimal refundFeePercentage;
}
