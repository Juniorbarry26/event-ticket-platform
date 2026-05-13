package com.alsaineybarry.tickets.services;

import com.alsaineybarry.tickets.domain.dtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrganizerAnalyticsService {
    
    /**
     * Get comprehensive analytics for a specific event
     */
    OrganizerAnalyticsDto getEventAnalytics(UUID eventId, UUID organizerId);
    
    /**
     * Get organizer dashboard with overall metrics
     */
    OrganizerDashboardDto getOrganizerDashboard(UUID organizerId);
    
    /**
     * Get all ticket purchasers for a specific event
     */
    Page<TicketPurchaserDto> getEventTicketPurchasers(UUID eventId, UUID organizerId, Pageable pageable);
    
    /**
     * Get sales breakdown by ticket type for an event
     */
    Page<TicketTypeSalesDto> getEventTicketTypeSales(UUID eventId, UUID organizerId, Pageable pageable);
    
    /**
     * Get revenue summary for all events of an organizer
     */
    RevenueSummaryDto getOrganizerRevenueSummary(UUID organizerId);
    
    /**
     * Get profit/loss analysis for a specific event
     */
    ProfitLossAnalysisDto getEventProfitLossAnalysis(UUID eventId, UUID organizerId);
}
