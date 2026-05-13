package com.alsaineybarry.tickets.controllers;

import com.alsaineybarry.tickets.domain.dtos.OrganizerOverallProfitDto;
import com.alsaineybarry.tickets.domain.dtos.OrganizerProfitSummaryDto;
import com.alsaineybarry.tickets.domain.dtos.OrganizerTicketPurchaseDto;
import com.alsaineybarry.tickets.domain.entities.User;
import com.alsaineybarry.tickets.services.OrganizerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/organizers")
@RequiredArgsConstructor
@Tag(name = "Organizer Management", description = "Endpoints for organizers to view tickets and profits")
public class OrganizerController {

    private final OrganizerService organizerService;

    @Operation(
            summary = "Get all ticket purchases for the authenticated organizer",
            description = "Retrieves a paginated list of all ticket purchases for events organized by the authenticated user"
    )
    @GetMapping("/tickets")
    @PreAuthorize("hasRole('ORGANIZER')")
    public Page<OrganizerTicketPurchaseDto> getTicketPurchases(
            Pageable pageable,
            @AuthenticationPrincipal User user) {
        return organizerService.getTicketPurchases(user.getId(), pageable);
    }

    @Operation(
            summary = "Get ticket purchases for a specific event",
            description = "Retrieves a paginated list of ticket purchases for a specific event organized by the authenticated user"
    )
    @GetMapping("/events/{eventId}/tickets")
    @PreAuthorize("hasRole('ORGANIZER')")
    public Page<OrganizerTicketPurchaseDto> getTicketPurchasesForEvent(
            @PathVariable UUID eventId,
            Pageable pageable,
            @AuthenticationPrincipal User user) {
        return organizerService.getTicketPurchasesForEvent(user.getId(), eventId, pageable);
    }

    @Operation(
            summary = "Get profit summary for a specific event",
            description = "Retrieves detailed profit information for a specific event organized by the authenticated user"
    )
    @GetMapping("/events/{eventId}/profits")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<OrganizerProfitSummaryDto> getProfitSummaryForEvent(
            @PathVariable UUID eventId,
            @AuthenticationPrincipal User user) {
        try {
            OrganizerProfitSummaryDto profitSummary = organizerService.getProfitSummaryForEvent(user.getId(), eventId);
            return ResponseEntity.ok(profitSummary);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Get overall profit summary",
            description = "Retrieves overall profit summary for all events organized by the authenticated user"
    )
    @GetMapping("/profits")
    @PreAuthorize("hasRole('ORGANIZER')")
    public OrganizerOverallProfitDto getOverallProfitSummary(@AuthenticationPrincipal User user) {
        return organizerService.getOverallProfitSummary(user.getId());
    }
}
