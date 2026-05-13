package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class AdminEmailCampaignRequestDto {
    private String campaignName;
    private String subject;
    private String content;
    private LocalDateTime scheduledFor;
    private String targetAudience; // ALL_USERS, ORGANIZERS, ATTENDEES, etc.
}
