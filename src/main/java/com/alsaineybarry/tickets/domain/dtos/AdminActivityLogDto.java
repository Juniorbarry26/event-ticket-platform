package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AdminActivityLogDto {
    private UUID logId;
    private String action; // USER_CREATED, EVENT_CREATED, TICKET_PURCHASED, etc.
    private String entityType; // USER, EVENT, TICKET, TRANSACTION
    private UUID entityId;
    private String entityName;
    private UUID performedBy;
    private String performedByName;
    private String description;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime timestamp;
}
