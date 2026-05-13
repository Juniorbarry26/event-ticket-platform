package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AdminEmailCampaignDto {
    private UUID campaignId;
    private String campaignName;
    private String subject;
    private String content;
    private String status; // DRAFT, SCHEDULED, SENT, FAILED
    private LocalDateTime createdAt;
    private LocalDateTime scheduledFor;
    private LocalDateTime sentAt;
    private String targetAudience;
    private Integer totalRecipients;
    private Integer openedCount;
    private Integer clickedCount;
}
