package com.alsaineybarry.tickets.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentActivityDto {
    private String activityType; // TICKET_SOLD, EVENT_CREATED, EVENT_UPDATED, etc.
    private String description;
    private LocalDateTime timestamp;
    private String eventName;
    private String purchaserName;
    private BigDecimal amount;
    private Integer ticketCount;
}
