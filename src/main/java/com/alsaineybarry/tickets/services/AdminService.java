package com.alsaineybarry.tickets.services;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AdminService {
    
    // Organizer management
    Page<AdminOrganizerDto> getAllOrganizers(Pageable pageable);
    AdminOrganizerDto getOrganizerById(UUID organizerId);
    AdminOrganizerStatisticsDto getOrganizerStatistics();
    List<AdminOrganizerDto> getTopPerformingOrganizers(int limit);
    AdminOrganizerStatsDto getOrganizerStats(UUID organizerId);
    AdminOrganizerDto verifyOrganizer(UUID organizerId);
    AdminOrganizerDto approveOrganizer(UUID organizerId);
    AdminOrganizerDto rejectOrganizer(UUID organizerId, String rejectionReason);
    
    // Admin Dashboard - Stats & Overview
    AdminPlatformStatsDto getPlatformStats();
    Page<AdminActivityLogDto> getActivityLog(Pageable pageable);
    
    // User management
    Page<AdminUserManagementDto> getAllUsers(Pageable pageable);
    AdminUserManagementDto getUserById(UUID userId);
    void blockUser(UUID userId);
    void unblockUser(UUID userId);
    void suspendUser(UUID userId);
    void unsuspendUser(UUID userId);
    void banUser(UUID userId);
    
    // System statistics
    Integer getTotalOrganizersCount();
    Integer getTotalUsersCount();
    Integer getNewOrganizersThisMonth();
    Integer getNewUsersThisMonth();
    
    // Payout management
    Page<AdminPayoutDto> getAllPayouts(Pageable pageable);
    AdminPayoutDto getPayoutById(UUID payoutId);
    AdminPayoutDto createPayout(AdminPayoutRequestDto payoutRequest);
    AdminPayoutDto processPayout(UUID payoutId);
    AdminPayoutDto completePayout(UUID payoutId);
    AdminPayoutDto failPayout(UUID payoutId, String failureReason);
    AdminPayoutStatisticsDto getPayoutStatistics();
    
    // Transaction management
    Page<AdminTransactionDto> getAllTransactions(Pageable pageable);
    AdminTransactionDto getTransactionById(UUID transactionId);
    
    // Financial management
    AdminFinancialsDto getFinancials();
    
    // Report management
    AdminReportDto.UserReportDto getUserReport();
    AdminReportDto.EventReportDto getEventReport();
    AdminReportDto.RevenueReportDto getRevenueReport();
    AdminReportDto.RefundReportDto getRefundReport();
    
    // Event management
    Page<AdminEventDto> getAllEvents(Pageable pageable);
    AdminEventDto getEventById(UUID eventId);
    void deleteEvent(UUID eventId);
    Page<AdminEventDto> getPendingEvents(Pageable pageable);
    AdminEventApprovalStatsDto getEventApprovalStats();
    AdminEventDto approveEvent(UUID eventId);
    AdminEventDto rejectEvent(UUID eventId, String rejectionReason);
    AdminEventDto disableEvent(UUID eventId);
    
    // Admin tickets
    Page<AdminTicketDto> getAllTickets(Pageable pageable);
    
    // Fee settings
    AdminFeeSettingsDto getFeeSettings();
    AdminFeeSettingsDto updateFeeSettings(AdminFeeSettingsDto settings);
    
    // Admin settings
    AdminSettingsDto getAdminSettings();
    AdminSettingsDto updateAdminSettings(AdminSettingsDto settings);
    
    // Check-in management
    AdminCheckInStatsDto getEventCheckInStats(UUID eventId);
    Page<AdminAttendeeDto> getEventAttendees(UUID eventId, Pageable pageable);
    byte[] exportAttendanceReport(UUID eventId);
    
    // Email management
    List<AdminEmailTemplateDto> getEmailTemplates();
    AdminEmailCampaignDto createEmailCampaign(AdminEmailCampaignRequestDto campaignRequest);
    Page<AdminEmailCampaignDto> getEmailCampaigns(Pageable pageable);
    Page<AdminEmailRecipientDto> getCampaignRecipients(UUID campaignId, Pageable pageable);
    
    // Refund management
    Page<AdminRefundDto> getRefundRequests(UUID eventId, Pageable pageable);
    AdminRefundDto approveRefund(UUID refundId);
    AdminRefundDto rejectRefund(UUID refundId, String rejectionReason);
    AdminRefundStatsDto getRefundStatistics(UUID eventId);
}
