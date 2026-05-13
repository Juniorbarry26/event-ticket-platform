package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AdminTicketDto {
    private UUID ticketId;
    private UUID eventId;
    private String eventTitle;
    private UUID userId;
    private String userName;
    private String userEmail;
    private UUID organizerId;
    private String organizerName;
    private String ticketTypeName;
    private BigDecimal price;
    private String currency;
    private String status; // ACTIVE, USED, REFUNDED, CANCELLED
    private LocalDateTime purchaseDate;
    private LocalDateTime usedDate;
    private LocalDateTime refundDate;
    private Boolean checkedIn;
    private LocalDateTime checkInTime;
}
