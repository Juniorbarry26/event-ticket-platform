package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ReportExportStatusDto {
    private UUID exportId;
    private String status; // PENDING, PROCESSING, COMPLETED, FAILED, EXPIRED
    private Integer progressPercentage;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private LocalDateTime expiresAt;
    private String downloadUrl;
}
