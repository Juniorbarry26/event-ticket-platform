package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AdminFinancialsDto {
    private BigDecimal totalRevenue;
    private BigDecimal totalPayouts;
    private BigDecimal netProfit;
    private BigDecimal pendingPayouts;
    private BigDecimal completedPayouts;
    private BigDecimal failedPayouts;
    private Integer totalTransactions;
    private Integer completedTransactions;
    private Integer pendingTransactions;
    private Integer failedTransactions;
    private BigDecimal averageTransactionAmount;
    private String currency;
    private LocalDateTime lastUpdated;
    private List<MonthlyFinancialSummaryDto> monthlySummaries;
    
    @Data
    @Builder
    public static class MonthlyFinancialSummaryDto {
        private String month;
        private BigDecimal revenue;
        private BigDecimal payouts;
        private BigDecimal profit;
        private Integer transactionCount;
    }
}
