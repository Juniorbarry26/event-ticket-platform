package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AdminTransactionDto {
    private UUID transactionId;
    private UUID userId;
    private String userName;
    private String userEmail;
    private UUID eventId;
    private String eventTitle;
    private UUID ticketId;
    private String ticketTypeName;
    private BigDecimal amount;
    private String currency;
    private String transactionType; // PURCHASE, REFUND, PAYOUT, etc.
    private String status; // COMPLETED, PENDING, FAILED, REFUNDED
    private String paymentMethod;
    private String transactionReference;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private String notes;
}
