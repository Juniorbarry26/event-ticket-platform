package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class RefundRequestDto {
    private UUID refundId;
    private UUID ticketId;
    @NotBlank
    @Size(max = 500)
    private String reason;
    private String status; // PENDING, APPROVED, REJECTED, PROCESSED
    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;
    private String processedBy;
    private BigDecimal refundAmount;
    private String currency;
    private String rejectionReason;
    private String paymentMethod;
}
