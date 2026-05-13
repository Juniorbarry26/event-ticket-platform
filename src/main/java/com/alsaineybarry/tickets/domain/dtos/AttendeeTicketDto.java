package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AttendeeTicketDto {
    private UUID ticketId;
    private UUID eventId;
    private String eventTitle;
    private LocalDateTime eventDate;
    private java.time.LocalTime eventTime;
    private String venue;
    private String address;
    private String ticketTypeName;
    private BigDecimal price;
    private String currency;
    private String status; // ACTIVE, USED, REFUNDED, CANCELLED
    private LocalDateTime purchaseDate;
    private String qrCodeUrl;
    private Boolean checkedIn;
    private LocalDateTime checkInTime;
    private String seatNumber;
    private String gateNumber;
    private Integer quantity;
}
