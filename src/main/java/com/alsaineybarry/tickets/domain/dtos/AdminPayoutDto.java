package com.alsaineybarry.tickets.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminPayoutDto {
    private UUID payoutId;
    private UUID organizerId;
    private String organizerName;
    private String organizerEmail;
    private BigDecimal amount;
    private String currency;
    private String status; // PENDING, PROCESSING, COMPLETED, FAILED
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private LocalDateTime completedAt;
    private String paymentMethod;
    private String transactionId;
    private String notes;
    private String failureReason;
}
