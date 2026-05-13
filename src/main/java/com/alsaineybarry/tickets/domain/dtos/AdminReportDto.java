package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class AdminReportDto {
    private String reportType;
    private LocalDateTime generatedAt;
    private String period;
    private Map<String, Object> summary;
    private List<ReportDataDto> details;
    
    @Data
    @Builder
    public static class ReportDataDto {
        private String label;
        private Object value;
        private String category;
        private LocalDateTime timestamp;
    }
    
    // Specific report types
    @Data
    @Builder
    public static class UserReportDto {
        private Integer totalUsers;
        private Integer activeUsers;
        private Integer newUsersThisMonth;
        private Integer blockedUsers;
        private List<UserActivityDto> userActivities;
        
        @Data
        @Builder
        public static class UserActivityDto {
            private String userName;
            private String userEmail;
            private String activityType;
            private Integer activityCount;
            private LocalDateTime lastActivity;
        }
    }
    
    @Data
    @Builder
    public static class EventReportDto {
        private Integer totalEvents;
        private Integer activeEvents;
        private Integer completedEvents;
        private Integer cancelledEvents;
        private Integer totalAttendees;
        private BigDecimal totalRevenue;
        private List<EventPerformanceDto> eventPerformances;
        
        @Data
        @Builder
        public static class EventPerformanceDto {
            private String eventTitle;
            private String organizerName;
            private Integer ticketsSold;
            private BigDecimal revenue;
            private Integer attendeeCount;
            private String status;
        }
    }
    
    @Data
    @Builder
    public static class RevenueReportDto {
        private BigDecimal totalRevenue;
        private BigDecimal monthlyRevenue;
        private BigDecimal weeklyRevenue;
        private BigDecimal dailyRevenue;
        private String currency;
        private List<RevenueBreakdownDto> revenueBreakdown;
        
        @Data
        @Builder
        public static class RevenueBreakdownDto {
            private String category;
            private BigDecimal amount;
            private Double percentage;
            private Integer transactionCount;
        }
    }
    
    @Data
    @Builder
    public static class RefundReportDto {
        private Integer totalRefunds;
        private BigDecimal totalRefundAmount;
        private String currency;
        private Double refundRate;
        private List<RefundDetailDto> refundDetails;
        
        @Data
        @Builder
        public static class RefundDetailDto {
            private String transactionId;
            private String userName;
            private String eventTitle;
            private BigDecimal refundAmount;
            private String refundReason;
            private LocalDateTime refundDate;
            private String status;
        }
    }
}
