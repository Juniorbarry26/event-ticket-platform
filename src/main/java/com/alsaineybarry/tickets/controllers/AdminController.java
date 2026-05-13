package com.alsaineybarry.tickets.controllers;

import com.alsaineybarry.tickets.domain.dtos.AdminActivityLogDto;
import com.alsaineybarry.tickets.domain.dtos.AdminAttendeeDto;
import com.alsaineybarry.tickets.domain.dtos.AdminCheckInStatsDto;
import com.alsaineybarry.tickets.domain.dtos.AdminEmailCampaignDto;
import com.alsaineybarry.tickets.domain.dtos.AdminEmailCampaignRequestDto;
import com.alsaineybarry.tickets.domain.dtos.AdminEmailRecipientDto;
import com.alsaineybarry.tickets.domain.dtos.AdminEmailTemplateDto;
import com.alsaineybarry.tickets.domain.dtos.AdminEventApprovalStatsDto;
import com.alsaineybarry.tickets.domain.dtos.AdminEventDto;
import com.alsaineybarry.tickets.domain.dtos.AdminFeeSettingsDto;
import com.alsaineybarry.tickets.domain.dtos.AdminFinancialsDto;
import com.alsaineybarry.tickets.domain.dtos.AdminOrganizerDto;
import com.alsaineybarry.tickets.domain.dtos.AdminOrganizerStatisticsDto;
import com.alsaineybarry.tickets.domain.dtos.AdminOrganizerStatsDto;
import com.alsaineybarry.tickets.domain.dtos.AdminPayoutDto;
import com.alsaineybarry.tickets.domain.dtos.AdminPayoutRequestDto;
import com.alsaineybarry.tickets.domain.dtos.AdminPayoutStatisticsDto;
import com.alsaineybarry.tickets.domain.dtos.AdminPlatformStatsDto;
import com.alsaineybarry.tickets.domain.dtos.AdminRefundDto;
import com.alsaineybarry.tickets.domain.dtos.AdminRefundStatsDto;
import com.alsaineybarry.tickets.domain.dtos.AdminReportDto;
import com.alsaineybarry.tickets.domain.dtos.AdminSettingsDto;
import com.alsaineybarry.tickets.domain.dtos.AdminTicketDto;
import com.alsaineybarry.tickets.domain.dtos.AdminTransactionDto;
import com.alsaineybarry.tickets.domain.dtos.AdminUserManagementDto;
import com.alsaineybarry.tickets.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Management", description = "Endpoints for administrators to manage users and view system statistics")
public class AdminController {

    private final AdminService adminService;

    // Organizer Management Endpoints
    @Operation(
            summary = "Get all organizers",
            description = "Retrieves a paginated list of all organizers with their statistics"
    )
    @GetMapping("/organizers")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<AdminOrganizerDto> getAllOrganizers(Pageable pageable) {
        return adminService.getAllOrganizers(pageable);
    }

    @Operation(
            summary = "Get organizer by ID",
            description = "Retrieves detailed information about a specific organizer"
    )
    @GetMapping("/organizers/{organizerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminOrganizerDto> getOrganizerById(@PathVariable UUID organizerId) {
        try {
            AdminOrganizerDto organizer = adminService.getOrganizerById(organizerId);
            return ResponseEntity.ok(organizer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Get organizer statistics",
            description = "Retrieves comprehensive statistics about all organizers and their performance"
    )
    @GetMapping("/organizers/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public AdminOrganizerStatisticsDto getOrganizerStatistics() {
        return adminService.getOrganizerStatistics();
    }

    @Operation(
            summary = "Get organizer statistics",
            description = "Retrieves detailed statistics for a specific organizer"
    )
    @GetMapping("/organizers/{organizerId}/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminOrganizerStatsDto> getOrganizerStats(@PathVariable UUID organizerId) {
        try {
            AdminOrganizerStatsDto stats = adminService.getOrganizerStats(organizerId);
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Verify an organizer",
            description = "Verifies an organizer account"
    )
    @PostMapping("/organizers/{organizerId}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminOrganizerDto> verifyOrganizer(@PathVariable UUID organizerId) {
        try {
            AdminOrganizerDto organizer = adminService.verifyOrganizer(organizerId);
            return ResponseEntity.ok(organizer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Approve organizer",
            description = "Approves an organizer account"
    )
    @PostMapping("/organizers/{organizerId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminOrganizerDto> approveOrganizer(@PathVariable UUID organizerId) {
        try {
            AdminOrganizerDto organizer = adminService.approveOrganizer(organizerId);
            return ResponseEntity.ok(organizer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Reject organizer",
            description = "Rejects an organizer account"
    )
    @PostMapping("/organizers/{organizerId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminOrganizerDto> rejectOrganizer(
            @PathVariable UUID organizerId,
            @RequestBody String rejectionReason) {
        try {
            AdminOrganizerDto organizer = adminService.rejectOrganizer(organizerId, rejectionReason);
            return ResponseEntity.ok(organizer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // User Management Endpoints
    @Operation(
            summary = "Get all users",
            description = "Retrieves a paginated list of all users in the system"
    )
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<AdminUserManagementDto> getAllUsers(Pageable pageable) {
        return adminService.getAllUsers(pageable);
    }

    @Operation(
            summary = "Get user by ID",
            description = "Retrieves detailed information about a specific user"
    )
    @GetMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminUserManagementDto> getUserById(@PathVariable UUID userId) {
        try {
            AdminUserManagementDto user = adminService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Block a user",
            description = "Blocks a user from accessing the system"
    )
    @PostMapping("/users/{userId}/block")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> blockUser(@PathVariable UUID userId) {
        try {
            adminService.blockUser(userId);
            return ResponseEntity.ok().build();
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.status(501).build(); // Not Implemented
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Suspend a user",
            description = "Suspends a user from accessing the system"
    )
    @PostMapping("/users/{userId}/suspend")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> suspendUser(@PathVariable UUID userId) {
        try {
            adminService.suspendUser(userId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Unsuspend a user",
            description = "Unsuspends a user and restores their access to the system"
    )
    @PostMapping("/users/{userId}/unsuspend")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> unsuspendUser(@PathVariable UUID userId) {
        try {
            adminService.unsuspendUser(userId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Ban a user",
            description = "Permanently bans a user from the system"
    )
    @PostMapping("/users/{userId}/ban")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> banUser(@PathVariable UUID userId) {
        try {
            adminService.banUser(userId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // System Statistics Endpoints
    @Operation(
            summary = "Get total organizers count",
            description = "Retrieves the total number of organizers in the system"
    )
    @GetMapping("/statistics/organizers/count")
    @PreAuthorize("hasRole('ADMIN')")
    public Integer getTotalOrganizersCount() {
        return adminService.getTotalOrganizersCount();
    }

    @Operation(
            summary = "Get total users count",
            description = "Retrieves the total number of users in the system"
    )
    @GetMapping("/statistics/users/count")
    @PreAuthorize("hasRole('ADMIN')")
    public Integer getTotalUsersCount() {
        return adminService.getTotalUsersCount();
    }

    @Operation(
            summary = "Get new organizers this month",
            description = "Retrieves the number of new organizers who joined this month"
    )
    @GetMapping("/statistics/organizers/new-this-month")
    @PreAuthorize("hasRole('ADMIN')")
    public Integer getNewOrganizersThisMonth() {
        return adminService.getNewOrganizersThisMonth();
    }

    @Operation(
            summary = "Get new users this month",
            description = "Retrieves the number of new users who joined this month"
    )
    @GetMapping("/statistics/users/new-this-month")
    @PreAuthorize("hasRole('ADMIN')")
    public Integer getNewUsersThisMonth() {
        return adminService.getNewUsersThisMonth();
    }

    @Operation(
            summary = "Get admin dashboard overview",
            description = "Retrieves a comprehensive overview of system statistics for the admin dashboard"
    )
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDashboardOverviewDto> getDashboardOverview() {
        try {
            AdminDashboardOverviewDto overview = AdminDashboardOverviewDto.builder()
                    .totalOrganizers(adminService.getTotalOrganizersCount())
                    .totalUsers(adminService.getTotalUsersCount())
                    .newOrganizersThisMonth(adminService.getNewOrganizersThisMonth())
                    .newUsersThisMonth(adminService.getNewUsersThisMonth())
                    .organizerStatistics(adminService.getOrganizerStatistics())
                    .build();
            return ResponseEntity.ok(overview);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Payout Management Endpoints
    @Operation(
            summary = "Get all payouts",
            description = "Retrieves a paginated list of all payouts with their details"
    )
    @GetMapping("/payouts")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<AdminPayoutDto> getAllPayouts(Pageable pageable) {
        return adminService.getAllPayouts(pageable);
    }

    @Operation(
            summary = "Get payout by ID",
            description = "Retrieves detailed information about a specific payout"
    )
    @GetMapping("/payouts/{payoutId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminPayoutDto> getPayoutById(@PathVariable UUID payoutId) {
        try {
            AdminPayoutDto payout = adminService.getPayoutById(payoutId);
            return ResponseEntity.ok(payout);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Create new payout",
            description = "Creates a new payout for an organizer"
    )
    @PostMapping("/payouts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminPayoutDto> createPayout(@RequestBody AdminPayoutRequestDto payoutRequest) {
        try {
            AdminPayoutDto payout = adminService.createPayout(payoutRequest);
            return ResponseEntity.ok(payout);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Process payout",
            description = "Processes a pending payout"
    )
    @PostMapping("/payouts/{payoutId}/process")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminPayoutDto> processPayout(@PathVariable UUID payoutId) {
        try {
            AdminPayoutDto payout = adminService.processPayout(payoutId);
            return ResponseEntity.ok(payout);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Complete payout",
            description = "Marks a payout as completed"
    )
    @PostMapping("/payouts/{payoutId}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminPayoutDto> completePayout(@PathVariable UUID payoutId) {
        try {
            AdminPayoutDto payout = adminService.completePayout(payoutId);
            return ResponseEntity.ok(payout);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Fail payout",
            description = "Marks a payout as failed with a reason"
    )
    @PostMapping("/payouts/{payoutId}/fail")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminPayoutDto> failPayout(
            @PathVariable UUID payoutId,
            @RequestBody String failureReason) {
        try {
            AdminPayoutDto payout = adminService.failPayout(payoutId, failureReason);
            return ResponseEntity.ok(payout);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Get payout statistics",
            description = "Retrieves comprehensive statistics about payouts"
    )
    @GetMapping("/payouts/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminPayoutStatisticsDto> getPayoutStatistics() {
        try {
            AdminPayoutStatisticsDto statistics = adminService.getPayoutStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Transaction Management Endpoints
    @Operation(
            summary = "Get all transactions",
            description = "Retrieves a paginated list of all transactions with their details"
    )
    @GetMapping("/transactions")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<AdminTransactionDto> getAllTransactions(Pageable pageable) {
        return adminService.getAllTransactions(pageable);
    }

    @Operation(
            summary = "Get transaction by ID",
            description = "Retrieves detailed information about a specific transaction"
    )
    @GetMapping("/transactions/{transactionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminTransactionDto> getTransactionById(@PathVariable UUID transactionId) {
        try {
            AdminTransactionDto transaction = adminService.getTransactionById(transactionId);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Admin Fee Settings Endpoints
    @Operation(
            summary = "Get fee settings",
            description = "Retrieves current platform fee settings"
    )
    @GetMapping("/fee-settings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminFeeSettingsDto> getFeeSettings() {
        try {
            AdminFeeSettingsDto settings = adminService.getFeeSettings();
            return ResponseEntity.ok(settings);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
            summary = "Update fee settings",
            description = "Updates platform fee settings"
    )
    @PutMapping("/fee-settings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminFeeSettingsDto> updateFeeSettings(@RequestBody AdminFeeSettingsDto settings) {
        try {
            AdminFeeSettingsDto updatedSettings = adminService.updateFeeSettings(settings);
            return ResponseEntity.ok(updatedSettings);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @Operation(
            summary = "Get financial overview",
            description = "Retrieves comprehensive financial data including revenue, payouts, and profit"
    )
    @GetMapping("/financials")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminFinancialsDto> getFinancials() {
        try {
            AdminFinancialsDto financials = adminService.getFinancials();
            return ResponseEntity.ok(financials);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Report Management Endpoints
    @Operation(
            summary = "Get user report",
            description = "Retrieves comprehensive report about users including statistics and activities"
    )
    @GetMapping("/reports/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminReportDto.UserReportDto> getUserReport() {
        try {
            AdminReportDto.UserReportDto userReport = adminService.getUserReport();
            return ResponseEntity.ok(userReport);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
            summary = "Get event report",
            description = "Retrieves comprehensive report about events including performance metrics"
    )
    @GetMapping("/reports/events")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminReportDto.EventReportDto> getEventReport() {
        try {
            AdminReportDto.EventReportDto eventReport = adminService.getEventReport();
            return ResponseEntity.ok(eventReport);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
            summary = "Get revenue report",
            description = "Retrieves comprehensive revenue report with breakdowns by category"
    )
    @GetMapping("/reports/revenue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminReportDto.RevenueReportDto> getRevenueReport() {
        try {
            AdminReportDto.RevenueReportDto revenueReport = adminService.getRevenueReport();
            return ResponseEntity.ok(revenueReport);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
            summary = "Get refund report",
            description = "Retrieves comprehensive report about refunds and refund statistics"
    )
    @GetMapping("/reports/refunds")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminReportDto.RefundReportDto> getRefundReport() {
        try {
            AdminReportDto.RefundReportDto refundReport = adminService.getRefundReport();
            return ResponseEntity.ok(refundReport);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Event Management Endpoints
    @Operation(
            summary = "Get all events",
            description = "Retrieves a paginated list of all events in the system"
    )
    @GetMapping("/events")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<AdminEventDto> getAllEvents(Pageable pageable) {
        return adminService.getAllEvents(pageable);
    }

    @Operation(
            summary = "Get event by ID",
            description = "Retrieves detailed information about a specific event"
    )
    @GetMapping("/events/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminEventDto> getEventById(@PathVariable UUID eventId) {
        try {
            AdminEventDto event = adminService.getEventById(eventId);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Get pending approval events",
            description = "Retrieves a paginated list of events pending admin approval"
    )
    @GetMapping("/events/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<AdminEventDto> getPendingEvents(Pageable pageable) {
        return adminService.getPendingEvents(pageable);
    }

    @Operation(
            summary = "Get event approval statistics",
            description = "Retrieves comprehensive statistics about event approvals"
    )
    @GetMapping("/events/approval-stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminEventApprovalStatsDto> getEventApprovalStats() {
        try {
            AdminEventApprovalStatsDto stats = adminService.getEventApprovalStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
            summary = "Approve an event",
            description = "Approves an event and makes it publicly available"
    )
    @PostMapping("/events/{eventId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminEventDto> approveEvent(@PathVariable UUID eventId) {
        try {
            AdminEventDto event = adminService.approveEvent(eventId);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Reject an event",
            description = "Rejects an event with a reason"
    )
    @PostMapping("/events/{eventId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminEventDto> rejectEvent(
            @PathVariable UUID eventId,
            @RequestBody String rejectionReason) {
        try {
            AdminEventDto event = adminService.rejectEvent(eventId, rejectionReason);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Disable an event",
            description = "Temporarily disables an event from public view"
    )
    @PostMapping("/events/{eventId}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminEventDto> disableEvent(@PathVariable UUID eventId) {
        try {
            AdminEventDto event = adminService.disableEvent(eventId);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Admin Tickets Endpoints
    @Operation(
            summary = "Get all tickets",
            description = "Retrieves a paginated list of all tickets in the system"
    )
    @GetMapping("/tickets")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<AdminTicketDto> getAllTickets(Pageable pageable) {
        return adminService.getAllTickets(pageable);
    }
    @Operation(
            summary = "Get check-in statistics for event",
            description = "Retrieves comprehensive check-in statistics for a specific event"
    )
    @GetMapping("/events/{eventId}/checkin/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminCheckInStatsDto> getEventCheckInStats(@PathVariable UUID eventId) {
        try {
            AdminCheckInStatsDto stats = adminService.getEventCheckInStats(eventId);
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Get event attendees",
            description = "Retrieves a paginated list of attendees for a specific event"
    )
    @GetMapping("/events/{eventId}/attendees")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<AdminAttendeeDto> getEventAttendees(
            @PathVariable UUID eventId,
            Pageable pageable) {
        return adminService.getEventAttendees(eventId, pageable);
    }

    @Operation(
            summary = "Export attendance report",
            description = "Exports attendance report for a specific event"
    )
    @GetMapping("/events/{eventId}/attendees/export")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportAttendanceReport(@PathVariable UUID eventId) {
        try {
            byte[] reportData = adminService.exportAttendanceReport(eventId);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=attendance-report-" + eventId + ".csv")
                    .header("Content-Type", "text/csv")
                    .body(reportData);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Email Management Endpoints
    @Operation(
            summary = "Get email templates",
            description = "Retrieves available email templates for campaigns"
    )
    @GetMapping("/email-templates")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AdminEmailTemplateDto> getEmailTemplates() {
        return adminService.getEmailTemplates();
    }

    @Operation(
            summary = "Create email campaign",
            description = "Creates a new email campaign for users"
    )
    @PostMapping("/email-campaigns")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminEmailCampaignDto> createEmailCampaign(
            @RequestBody AdminEmailCampaignRequestDto campaignRequest) {
        try {
            AdminEmailCampaignDto campaign = adminService.createEmailCampaign(campaignRequest);
            return ResponseEntity.ok(campaign);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Get email campaigns",
            description = "Retrieves a paginated list of email campaigns"
    )
    @GetMapping("/email-campaigns")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<AdminEmailCampaignDto> getEmailCampaigns(Pageable pageable) {
        return adminService.getEmailCampaigns(pageable);
    }

    @Operation(
            summary = "Get campaign recipients",
            description = "Retrieves a paginated list of recipients for a specific campaign"
    )
    @GetMapping("/email-campaigns/{campaignId}/recipients")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<AdminEmailRecipientDto> getCampaignRecipients(
            @PathVariable UUID campaignId,
            Pageable pageable) {
        return adminService.getCampaignRecipients(campaignId, pageable);
    }

    // Refund Management Endpoints
    @Operation(
            summary = "Get refund requests",
            description = "Retrieves a paginated list of refund requests"
    )
    @GetMapping("/refunds")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<AdminRefundDto> getRefundRequests(
            @RequestParam(required = false) UUID eventId,
            Pageable pageable) {
        return adminService.getRefundRequests(eventId, pageable);
    }

    @Operation(
            summary = "Approve refund",
            description = "Approves a refund request"
    )
    @PostMapping("/refunds/{refundId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminRefundDto> approveRefund(@PathVariable UUID refundId) {
        try {
            AdminRefundDto refund = adminService.approveRefund(refundId);
            return ResponseEntity.ok(refund);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Reject refund",
            description = "Rejects a refund request"
    )
    @PostMapping("/refunds/{refundId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminRefundDto> rejectRefund(
            @PathVariable UUID refundId,
            @RequestBody String rejectionReason) {
        try {
            AdminRefundDto refund = adminService.rejectRefund(refundId, rejectionReason);
            return ResponseEntity.ok(refund);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Admin Settings Endpoints
    @Operation(
            summary = "Get admin settings",
            description = "Retrieves current admin platform settings"
    )
    @GetMapping("/settings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminSettingsDto> getAdminSettings() {
        try {
            AdminSettingsDto settings = adminService.getAdminSettings();
            return ResponseEntity.ok(settings);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
            summary = "Update admin settings",
            description = "Updates admin platform settings"
    )
    @PutMapping("/settings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminSettingsDto> updateAdminSettings(@RequestBody AdminSettingsDto settings) {
        try {
            AdminSettingsDto updatedSettings = adminService.updateAdminSettings(settings);
            return ResponseEntity.ok(updatedSettings);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Admin Dashboard - Stats & Overview
    @Operation(
            summary = "Get overall platform statistics",
            description = "Retrieves comprehensive platform statistics including total users, events, tickets, and revenue"
    )
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminPlatformStatsDto> getPlatformStats() {
        try {
            AdminPlatformStatsDto stats = adminService.getPlatformStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
            summary = "Get activity logs",
            description = "Retrieves paginated activity logs for admin monitoring"
    )
    @GetMapping("/activity-log")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<AdminActivityLogDto> getActivityLog(Pageable pageable) {
        return adminService.getActivityLog(pageable);
    }
    @lombok.Builder
    @lombok.Data
    public static class AdminDashboardOverviewDto {
        private Integer totalOrganizers;
        private Integer totalUsers;
        private Integer newOrganizersThisMonth;
        private Integer newUsersThisMonth;
        private AdminOrganizerStatisticsDto organizerStatistics;
    }
}
