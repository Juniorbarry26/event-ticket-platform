package com.alsaineybarry.tickets.services;

import com.alsaineybarry.tickets.domain.dtos.OrganizerOverallProfitDto;
import com.alsaineybarry.tickets.domain.dtos.OrganizerProfitSummaryDto;
import com.alsaineybarry.tickets.domain.dtos.OrganizerTicketPurchaseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrganizerService {
    
    Page<OrganizerTicketPurchaseDto> getTicketPurchases(UUID organizerId, Pageable pageable);
    
    Page<OrganizerTicketPurchaseDto> getTicketPurchasesForEvent(UUID organizerId, UUID eventId, Pageable pageable);
    
    OrganizerProfitSummaryDto getProfitSummaryForEvent(UUID organizerId, UUID eventId);
    
    OrganizerOverallProfitDto getOverallProfitSummary(UUID organizerId);
}
