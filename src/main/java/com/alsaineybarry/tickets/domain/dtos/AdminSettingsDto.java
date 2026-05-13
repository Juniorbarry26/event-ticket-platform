package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class AdminSettingsDto {
    private String platformName;
    private String platformEmail;
    private String supportEmail;
    private String contactPhone;
    private String timezone;
    private String currency;
    private Boolean enableEventApproval;
    private Boolean enableOrganizerVerification;
    private Boolean enableEmailNotifications;
    private Boolean enableSmsNotifications;
    private Integer maxEventsPerOrganizer;
    private Integer maxTicketsPerEvent;
    private Boolean enableRefunds;
    private Integer refundWindowDays;
    private Boolean enableTransfers;
    private Integer transferWindowHours;
    private Boolean enableWaitlist;
    private String maintenanceMode;
    private LocalDateTime maintenanceStartTime;
    private LocalDateTime maintenanceEndTime;
    private String maintenanceMessage;
    private Boolean enableAnalytics;
    private Boolean enableAuditLogs;
    private Integer auditLogRetentionDays;
}
