package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ReportExportResponseDto {
    private String downloadUrl;
    private String fileName;
    private String format; // CSV, EXCEL, PDF
    private Long fileSize;
    private String contentType;
    private LocalDateTime generatedAt;
    private LocalDateTime expiresAt;
    private Integer recordCount;
}
