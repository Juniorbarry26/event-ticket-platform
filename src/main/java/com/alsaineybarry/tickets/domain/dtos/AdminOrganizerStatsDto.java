package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class AdminOrganizerStatsDto {
    private UUID organizerId;
    private String organizerName;
    private String organizerEmail;
    private Integer totalEvents;
    private Integer activeEvents;
    private Integer completedEvents;
    private Integer cancelledEvents;
    private Integer totalTicketsSold;
    private BigDecimal totalRevenue;
    private BigDecimal averageTicketPrice;
    private BigDecimal totalFeesPaid;
    private BigDecimal netEarnings;
    private Double averageAttendanceRate;
    private Integer repeatCustomers;
    private LocalDateTime lastEventDate;
    private LocalDateTime joinedDate;
    private Boolean isVerified;
    private Boolean isApproved;
    private List<AdminOrganizerStatsDto.EventPerformanceDto> recentEvents;
    
    @Data
    @Builder
    public static class EventPerformanceDto {
        private UUID eventId;
        private String eventTitle;
        private LocalDateTime eventDate;
        private Integer ticketsSold;
        private Integer totalTickets;
        private BigDecimal revenue;
        private Double attendanceRate;
        private String status;
    }
}
