package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class AdminEventApprovalStatsDto {
    private Integer totalEvents;
    private Integer pendingEvents;
    private Integer approvedEvents;
    private Integer rejectedEvents;
    private Integer disabledEvents;
    private Double approvalRate;
    private Double rejectionRate;
    private Integer pendingThisWeek;
    private Integer approvedThisWeek;
    private Integer rejectedThisWeek;
    private LocalDateTime lastUpdated;
}
