package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AdminEventDto {
    private UUID eventId;
    private String eventTitle;
    private String organizerName;
    private String organizerEmail;
    private LocalDateTime eventDate;
    private String venue;
    private Integer totalTickets;
    private Integer ticketsSold;
    private BigDecimal revenue;
    private String status; // ACTIVE, COMPLETED, CANCELLED
    private LocalDateTime createdAt;
}
