package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TicketQrCodeDto {
    private UUID ticketId;
    private String qrCodeData;
    private String qrCodeUrl;
    private LocalDateTime expiresAt;
    private LocalDateTime generatedAt;
}
