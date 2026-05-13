package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AttendeeEventDto {
    private UUID eventId;
    private String title;
    private LocalDateTime eventDate;
    private String venue;
    private Integer ticketsOwned;
    private Integer totalTickets;
    private String status; // UPCOMING, COMPLETED, CANCELLED
    private Boolean attended;
    private LocalDateTime checkedInAt;
}
