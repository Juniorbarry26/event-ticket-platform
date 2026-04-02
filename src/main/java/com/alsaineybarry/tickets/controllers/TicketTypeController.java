package com.alsaineybarry.tickets.controllers;

import com.alsaineybarry.tickets.domain.entities.User;
import com.alsaineybarry.tickets.services.TicketTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events/{eventId}/ticket-types")
@RequiredArgsConstructor
@Tag(name = "Ticket Type Management", description = "Endpoints for ticket purchases")
public class TicketTypeController {

    private final TicketTypeService ticketTypeService;

    @Operation(
            summary = "Purchase a ticket",
            description = "Purchases a ticket of a specific type for the authenticated user"
    )
    @PostMapping("/{ticketTypeId}/tickets")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> purchaseTicket(
            @PathVariable UUID ticketTypeId,
            @AuthenticationPrincipal User user) {
        ticketTypeService.purchaseTicket(user.getId(), ticketTypeId);
        return ResponseEntity.noContent().build();
    }
}