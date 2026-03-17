package com.alsaineybarry.tickets.controllers;

import com.alsaineybarry.tickets.domain.dtos.TicketValidationRequestDto;
import com.alsaineybarry.tickets.domain.dtos.TicketValidationResponseDto;
import com.alsaineybarry.tickets.domain.entities.TicketValidation;
import com.alsaineybarry.tickets.domain.entities.TicketValidationMethod;
import com.alsaineybarry.tickets.mappers.TicketValidationMapper;
import com.alsaineybarry.tickets.services.TicketValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/v1/ticket-validations")
@RequiredArgsConstructor
@Tag(name = "Ticket Validation", description = "APIs for validating tickets at events")
public class TicketValidationController {

    private final TicketValidationService ticketValidationService;
    private final TicketValidationMapper ticketValidationMapper;

    @PostMapping
    @Operation(summary = "Validate a ticket", description = "Validates a ticket using either manual entry or QR code scanning")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ticket validated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid validation request"),
        @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    public ResponseEntity<TicketValidationResponseDto> validateTicket(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Ticket validation request containing method and ticket ID")
            @RequestBody TicketValidationRequestDto ticketValidationRequestDto
    ){
        TicketValidationMethod method = ticketValidationRequestDto.getMethod();
        TicketValidation ticketValidation;
        if(TicketValidationMethod.MANUAL.equals(method)) {
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