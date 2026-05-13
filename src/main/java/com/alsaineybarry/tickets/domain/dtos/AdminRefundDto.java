package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AdminRefundDto {
    private UUID refundId;
    private UUID transactionId;
    private String userName;
    private String userEmail;
    private String eventTitle;
    private BigDecimal refundAmount;
    private String refundReason;
    private String rejectionReason;
    private String status; // PENDING, APPROVED, REJECTED, PROCESSED
    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;
}
