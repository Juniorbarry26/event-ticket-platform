package com.alsaineybarry.tickets.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizerTicketPurchaseDto {
    
    private UUID ticketId;
    private String ticketTypeName;
    private String eventName;
    private String purchaserName;
    private String purchaserEmail;
    private Double ticketPrice;
    private LocalDateTime purchaseDate;
    private String ticketStatus;
    
}
