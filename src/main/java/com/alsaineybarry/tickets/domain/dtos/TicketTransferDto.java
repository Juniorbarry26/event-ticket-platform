package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TicketTransferDto {
    private UUID transferId;
    private UUID ticketId;
    private UUID fromUserId;
    private String fromUserName;
    private String toUserEmail;
    private String toUserName;
    private UUID toUserId;
    private String status; // PENDING, ACCEPTED, REJECTED, COMPLETED, EXPIRED
    private LocalDateTime initiatedAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime completedAt;
    private LocalDateTime expiresAt;
    private String message;
}
