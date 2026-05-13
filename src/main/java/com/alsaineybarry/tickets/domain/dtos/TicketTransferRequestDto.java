package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
public class TicketTransferRequestDto {
    @NotBlank
    @Email
    private String toUserEmail;
    
    private String message;
    private Boolean requireConfirmation;
}
