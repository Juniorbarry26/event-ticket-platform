package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PublicEventDto {
    private UUID eventId;
    private String title;
    private String description;
    private String venue;
    private String address;
    private LocalDateTime eventDate;
    private java.time.LocalTime eventTime;
    private String imageUrl;
    private String organizerName;
    private String organizerEmail;
    private Integer totalTickets;
    private Integer availableTickets;
    private List<TicketTypeInfo> ticketTypes;
    private String status; // PUBLISHED, DRAFT, CANCELLED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @Data
    @Builder
    public static class TicketTypeInfo {
        private UUID ticketTypeId;
        private String name;
        private String description;
        private java.math.BigDecimal price;
        private Integer availableTickets;
        private Integer maxTicketsPerPerson;
    }
}
