package com.alsaineybarry.tickets.domain.dtos;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class AttendeeEventDetailsDto {
    private UUID eventId;
    private String title;
    private String description;
    private LocalDateTime eventDate;
    private java.time.LocalTime eventTime;
    private String venue;
    private String address;
    private String organizerName;
    private String organizerEmail;
    private Integer ticketsOwned;
    private List<OwnedTicketType> ticketTypes;
    private Boolean canTransfer;
    private Boolean canRequestRefund;
    private String refundPolicy;
    private LocalDateTime refundDeadline;
    
    @Data
    @Builder
    public static class OwnedTicketType {
        private String ticketTypeName;
        private Integer quantity;
        private BigDecimal price;
        private String currency;
    }
}
