package com.alsaineybarry.tickets.domain.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.alsaineybarry.tickets.domain.enums.TicketValidationStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketValidationResponseDto {
    private UUID ticketId;
    private TicketValidationStatusEnum status;
    private String userEmail;
    private String userName;
    private LocalDateTime validatedAt;
}