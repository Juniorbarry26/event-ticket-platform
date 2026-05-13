package com.alsaineybarry.tickets.services;

import com.alsaineybarry.tickets.domain.dtos.ReportExportRequestDto;
import com.alsaineybarry.tickets.domain.dtos.ReportExportResponseDto;
import com.alsaineybarry.tickets.domain.dtos.ReportExportStatusDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ReportExportService {
    
    // Export request management
    ReportExportResponseDto createExportRequest(ReportExportRequestDto exportRequest);
    ReportExportStatusDto getExportStatus(UUID exportId);
    Page<ReportExportStatusDto> getExportHistory(Pageable pageable);
    
    // Direct export methods
    byte[] exportRevenueReport(ReportExportRequestDto exportRequest);
    byte[] exportUserReport(ReportExportRequestDto exportRequest);
    byte[] exportEventReport(ReportExportRequestDto exportRequest);
    byte[] exportRefundReport(ReportExportRequestDto exportRequest);
    byte[] exportPayoutReport(ReportExportRequestDto exportRequest);
    
    // Utility methods
    void deleteExpiredExports();
    String generateDownloadUrl(UUID exportId, String fileName);
}
