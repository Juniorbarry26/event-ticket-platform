package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AdminEmailRecipientDto {
    private UUID recipientId;
    private UUID campaignId;
    private String userName;
    private String userEmail;
    private String status; // PENDING, SENT, DELIVERED, OPENED, CLICKED, BOUNCE, FAILED
    private LocalDateTime sentAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime openedAt;
    private LocalDateTime clickedAt;
    private String bounceReason;
}
