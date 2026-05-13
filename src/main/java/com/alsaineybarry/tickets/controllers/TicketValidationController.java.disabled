package com.alsaineybarry.tickets.controllers;

import com.alsaineybarry.tickets.domain.dtos.TicketValidationRequestDto;
import com.alsaineybarry.tickets.domain.dtos.TicketValidationResponseDto;
import com.alsaineybarry.tickets.domain.entities.TicketValidation;
import com.alsaineybarry.tickets.domain.entities.TicketValidationMethod;
import com.alsaineybarry.tickets.domain.entities.User;
import com.alsaineybarry.tickets.mappers.TicketValidationMapper;
import com.alsaineybarry.tickets.services.TicketValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ticket-validations")
@RequiredArgsConstructor
@Tag(name = "Ticket Validation", description = "Endpoints for ticket validation at events")
public class TicketValidationController {

    private final TicketValidationService ticketValidationService;
    private final TicketValidationMapper ticketValidationMapper;

    @Operation(
            summary = "Validate a ticket",
            description = "Validates a ticket using either manual entry or QR code scanning. Only organizers can validate tickets."
    )
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    public ResponseEntity<TicketValidationResponseDto> validateTicket(
            @Valid @RequestBody TicketValidationRequestDto ticketValidationRequestDto,
            @AuthenticationPrincipal User user) {
        
        TicketValidationMethod method = ticketValidationRequestDto.getMethod();
        TicketValidation ticketValidation;
        
        if (TicketValidationMethod.MANUAL.equals(method)) {
            ticketValidation = ticketValidationService.validateTicketManually(
                    ticketValidationRequestDto.getId());
        } else {
            ticketValidation = ticketValidationService.validateTicketByQrCode(
                    ticketValidationRequestDto.getId()
            );
        }
        
        return ResponseEntity.ok(
                ticketValidationMapper.toTicketValidationResponseDto(ticketValidation)
        );
    }
}