package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TicketValidationDto {
    private UUID ticketId;
    private Boolean isValid;
    private LocalDateTime validationTime;
    private String ticketHolderName;
    private String eventTitle;
    private String ticketType;
    private String status; // VALID, INVALID, USED, EXPIRED
    private String message;
    private String validatorName;
    private String gateNumber;
}
