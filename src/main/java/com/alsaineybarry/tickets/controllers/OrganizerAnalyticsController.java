package com.alsaineybarry.tickets.controllers;

import com.alsaineybarry.tickets.domain.dtos.*;
import com.alsaineybarry.tickets.repositories.UserRepository;
import com.alsaineybarry.tickets.services.OrganizerAnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/organizer/analytics")
@RequiredArgsConstructor
@Tag(name = "Organizer Analytics", description = "Analytics endpoints for event organizers")
public class OrganizerAnalyticsController {

    private final OrganizerAnalyticsService organizerAnalyticsService;
    private final UserRepository userRepository;

    @GetMapping("/dashboard")
    @Operation(summary = "Get organizer dashboard", description = "Get comprehensive dashboard with overall metrics for the organizer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboard retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized - only organizers can access their analytics"),
            @ApiResponse(responseCode = "404", description = "Organizer not found")
    })
    public ResponseEntity<OrganizerDashboardDto> getOrganizerDashboard(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        // Extract organizer ID from authenticated user
        UUID organizerId = extractUserIdFromUserDetails(userDetails);
        
        OrganizerDashboardDto dashboard = organizerAnalyticsService.getOrganizerDashboard(organizerId);
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/events/{eventId}")
    @Operation(summary = "Get event analytics", description = "Get comprehensive analytics for a specific event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event analytics retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized - organizer doesn't own this event"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    public ResponseEntity<OrganizerAnalyticsDto> getEventAnalytics(
            @Parameter(description = "Event ID", required = true)
            @PathVariable UUID eventId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        UUID organizerId = extractUserIdFromUserDetails(userDetails);
        
        OrganizerAnalyticsDto analytics = organizerAnalyticsService.getEventAnalytics(eventId, organizerId);
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/events/{eventId}/purchasers")
    @Operation(summary = "Get event ticket purchasers", description = "Get paginated list of all ticket purchasers for a specific event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket purchasers retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized - organizer doesn't own this event"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    public ResponseEntity<Page<TicketPurchaserDto>> getEventTicketPurchasers(
            @Parameter(description = "Event ID", required = true)
            @PathVariable UUID eventId,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "purchaseDate")
            @RequestParam(defaultValue = "purchaseDate") String sortBy,
            @Parameter(description = "Sort direction", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDir,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        UUID organizerId = extractUserIdFromUserDetails(userDetails);
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<TicketPurchaserDto> purchasers = organizerAnalyticsService
                .getEventTicketPurchasers(eventId, organizerId, pageable);
        
        return ResponseEntity.ok(purchasers);
    }

    @GetMapping("/events/{eventId}/ticket-types")
    @Operation(summary = "Get event ticket type sales", description = "Get sales breakdown by ticket type for a specific event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket type sales retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized - organizer doesn't own this event"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    public ResponseEntity<Page<TicketTypeSalesDto>> getEventTicketTypeSales(
            @Parameter(description = "Event ID", required = true)
            @PathVariable UUID eventId,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field", example = "revenue")
            @RequestParam(defaultValue = "revenue") String sortBy,
            @Parameter(description = "Sort direction", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDir,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        UUID organizerId = extractUserIdFromUserDetails(userDetails);
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<TicketTypeSalesDto> ticketTypeSales = organizerAnalyticsService
                .getEventTicketTypeSales(eventId, organizerId, pageable);
        
        return ResponseEntity.ok(ticketTypeSales);
    }

    @GetMapping("/revenue-summary")
    @Operation(summary = "Get revenue summary", description = "Get comprehensive revenue summary for all organizer's events")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Revenue summary retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized - only organizers can access their analytics"),
            @ApiResponse(responseCode = "404", description = "Organizer not found")
    })
    public ResponseEntity<RevenueSummaryDto> getOrganizerRevenueSummary(
            @AuthenticationPrincipal UserDetails userDetails) {
        
        UUID organizerId = extractUserIdFromUserDetails(userDetails);
        
        RevenueSummaryDto revenueSummary = organizerAnalyticsService
                .getOrganizerRevenueSummary(organizerId);
        
        return ResponseEntity.ok(revenueSummary);
    }

    @GetMapping("/events/{eventId}/profit-loss")
    @Operation(summary = "Get profit/loss analysis", description = "Get detailed profit/loss analysis for a specific event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profit/loss analysis retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized - organizer doesn't own this event"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    public ResponseEntity<ProfitLossAnalysisDto> getEventProfitLossAnalysis(
            @Parameter(description = "Event ID", required = true)
            @PathVariable UUID eventId,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        UUID organizerId = extractUserIdFromUserDetails(userDetails);
        
        ProfitLossAnalysisDto profitLossAnalysis = organizerAnalyticsService
                .getEventProfitLossAnalysis(eventId, organizerId);
        
        return ResponseEntity.ok(profitLossAnalysis);
    }

    /**
     * Extract user ID from UserDetails by looking up the user by email.
     * The username in UserDetails is the email address.
     */
    private UUID extractUserIdFromUserDetails(UserDetails userDetails) {
        String email = userDetails.getUsername();
        return userRepository.findByEmail(email)
                .map(user -> user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }
}
