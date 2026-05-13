package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AttendeeStatsDto {
    private Integer totalTickets;
    private Integer upcomingEvents;
    private Integer pastEvents;
    private BigDecimal totalSpent;
    private BigDecimal averageTicketPrice;
    private String favoriteEventCategory;
    private LocalDateTime lastEventAttended;
    private LocalDateTime nextUpcomingEvent;
    private Integer eventsAttendedThisMonth;
    private Integer eventsAttendedThisYear;
}
