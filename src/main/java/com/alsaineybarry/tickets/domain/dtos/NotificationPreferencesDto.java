package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationPreferencesDto {
    private Boolean emailNotifications;
    private Boolean smsNotifications;
    private Boolean pushNotifications;
    private Boolean eventReminders;
    private Boolean ticketUpdates;
    private Boolean refundUpdates;
    private Boolean transferUpdates;
    private Boolean promotionalEmails;
    private Boolean newsletterSubscription;
    private Integer reminderHoursBeforeEvent;
    private String preferredLanguage;
}
