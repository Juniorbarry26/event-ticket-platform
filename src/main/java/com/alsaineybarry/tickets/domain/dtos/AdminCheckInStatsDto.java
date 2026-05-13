package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class AdminCheckInStatsDto {
    private UUID eventId;
    private Integer totalTickets;
    private Integer checkedIn;
    private Integer noShows;
    private Integer pending;
    private Double checkInRate;
}
