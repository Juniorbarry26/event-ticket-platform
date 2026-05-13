package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AdminAttendeeDto {
    private UUID attendeeId;
    private UUID ticketId;
    private String userName;
    private String userEmail;
    private String ticketType;
    private LocalDateTime purchaseDate;
    private Boolean checkedIn;
    private LocalDateTime checkInTime;
}
