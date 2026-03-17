package com.alsaineybarry.tickets.controllers;

import static com.alsaineybarry.tickets.util.JwtUtil.parseUserId;

import java.util.UUID;

import com.alsaineybarry.tickets.services.TicketTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/events/{eventId}/ticket-types")
@Tag(name = "Ticket Type Management", description = "APIs for managing ticket types and purchases")
@SecurityRequirement(name = "bearerAuth")
public class TicketTypeController {

    private final TicketTypeService ticketTypeService;

    @PostMapping(path = "/{ticketTypeId}/tickets")
    @Operation(summary = "Purchase a ticket", description = "Purchases a ticket of a specific type for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Ticket purchased successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid ticket type or no availability"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Ticket type not found")
    })
    public ResponseEntity<Void> purchaseTicket(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "Ticket Type ID", required = true) @PathVariable UUID ticketTypeId
    ) {
        ticketTypeService.purchaseTicket(parseUserId(jwt), ticketTypeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}