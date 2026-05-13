package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ReportExportRequestDto {
    private UUID reportId;
    private String reportType; // REVENUE, USERS, EVENTS, REFUNDS, PAYOUTS
    private String format; // CSV, EXCEL, PDF
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String filters; // JSON string with additional filters
    private Boolean includeHeaders;
    private String timezone;
}
