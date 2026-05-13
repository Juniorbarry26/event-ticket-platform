package com.alsaineybarry.tickets.services.impl;

import com.alsaineybarry.tickets.domain.dtos.OrganizerOverallProfitDto;
import com.alsaineybarry.tickets.domain.dtos.OrganizerProfitSummaryDto;
import com.alsaineybarry.tickets.domain.dtos.OrganizerTicketPurchaseDto;
import com.alsaineybarry.tickets.domain.entities.Ticket;
import com.alsaineybarry.tickets.domain.entities.TicketType;
import com.alsaineybarry.tickets.domain.enums.TIcketStatusEnum;
import com.alsaineybarry.tickets.repositories.EventRepository;
import com.alsaineybarry.tickets.repositories.TicketRepository;
import com.alsaineybarry.tickets.repositories.TicketTypeRepository;
import com.alsaineybarry.tickets.services.OrganizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizerServiceImpl implements OrganizerService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final TicketTypeRepository ticketTypeRepository;

    @Override
    public Page<OrganizerTicketPurchaseDto> getTicketPurchases(UUID organizerId, Pageable pageable) {
        Page<Ticket> tickets = ticketRepository.findByEventOrganizerId(organizerId, pageable);
        return tickets.map(this::convertToOrganizerTicketPurchaseDto);
    }

    @Override
    public Page<OrganizerTicketPurchaseDto> getTicketPurchasesForEvent(UUID organizerId, UUID eventId, Pageable pageable) {
        Page<Ticket> tickets = ticketRepository.findByEventOrganizerIdAndEventId(organizerId, eventId, pageable);
        return tickets.map(this::convertToOrganizerTicketPurchaseDto);
    }

    @Override
    public OrganizerProfitSummaryDto getProfitSummaryForEvent(UUID organizerId, UUID eventId) {
        var event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!event.getOrganizer().getId().equals(organizerId)) {
            throw new RuntimeException("Event does not belong to this organizer");
        }

        List<Ticket> allTickets = ticketRepository.findByEventOrganizerIdAndEventId(organizerId, eventId, 
                org.springframework.data.domain.Pageable.unpaged()).getContent();

        Map<UUID, List<Ticket>> ticketsByType = allTickets.stream()
                .collect(Collectors.groupingBy(ticket -> ticket.getTicketType().getId()));

        List<OrganizerProfitSummaryDto.TicketTypeProfitDto> ticketTypeProfits = ticketsByType.entrySet().stream()
                .map(entry -> {
                    UUID ticketTypeId = entry.getKey();
                    List<Ticket> tickets = entry.getValue();
                    TicketType ticketType = tickets.get(0).getTicketType();
                    
                    int ticketsSold = tickets.size();
                    double revenue = ticketsSold * ticketType.getPrice();
                    double profit = revenue; // Assuming profit = revenue for now, can be adjusted later

                    return OrganizerProfitSummaryDto.TicketTypeProfitDto.builder()
                            .ticketTypeId(ticketTypeId)
                            .ticketTypeName(ticketType.getName())
                            .ticketPrice(ticketType.getPrice())
                            .ticketsSold(ticketsSold)
                            .revenue(revenue)
                            .profit(profit)
                            .build();
                })
                .collect(Collectors.toList());

        int totalTicketsSold = allTickets.size();
        double totalRevenue = ticketTypeProfits.stream()
                .mapToDouble(OrganizerProfitSummaryDto.TicketTypeProfitDto::getRevenue)
                .sum();
        double totalProfit = ticketTypeProfits.stream()
                .mapToDouble(OrganizerProfitSummaryDto.TicketTypeProfitDto::getProfit)
                .sum();

        return OrganizerProfitSummaryDto.builder()
                .eventId(eventId)
                .eventName(event.getName())
                .eventStartDate(event.getStart())
                .eventEndDate(event.getEnd())
                .totalTicketsSold(totalTicketsSold)
                .totalRevenue(totalRevenue)
                .totalProfit(totalProfit)
                .ticketTypeProfits(ticketTypeProfits)
                .build();
    }

    @Override
    public OrganizerOverallProfitDto getOverallProfitSummary(UUID organizerId) {
        List<Ticket> allTickets = ticketRepository.findByEventOrganizerId(organizerId, 
                org.springframework.data.domain.Pageable.unpaged()).getContent();

        Map<UUID, List<Ticket>> ticketsByEvent = allTickets.stream()
                .collect(Collectors.groupingBy(ticket -> ticket.getTicketType().getEvent().getId()));

        List<OrganizerProfitSummaryDto> eventProfits = ticketsByEvent.entrySet().stream()
                .map(entry -> {
                    UUID eventId = entry.getKey();
                    return getProfitSummaryForEvent(organizerId, eventId);
                })
                .collect(Collectors.toList());

        int totalEvents = eventProfits.size();
        int totalTicketsSold = eventProfits.stream()
                .mapToInt(OrganizerProfitSummaryDto::getTotalTicketsSold)
                .sum();
        double totalRevenue = eventProfits.stream()
                .mapToDouble(OrganizerProfitSummaryDto::getTotalRevenue)
                .sum();
        double totalProfit = eventProfits.stream()
                .mapToDouble(OrganizerProfitSummaryDto::getTotalProfit)
                .sum();

        return OrganizerOverallProfitDto.builder()
                .totalEvents(totalEvents)
                .totalTicketsSold(totalTicketsSold)
                .totalRevenue(totalRevenue)
                .totalProfit(totalProfit)
                .eventProfits(eventProfits)
                .build();
    }

    private OrganizerTicketPurchaseDto convertToOrganizerTicketPurchaseDto(Ticket ticket) {
        return OrganizerTicketPurchaseDto.builder()
                .ticketId(ticket.getId())
                .ticketTypeName(ticket.getTicketType().getName())
                .eventName(ticket.getTicketType().getEvent().getName())
                .purchaserName(ticket.getPurchaser().getName())
                .purchaserEmail(ticket.getPurchaser().getEmail())
                .ticketPrice(ticket.getTicketType().getPrice())
                .purchaseDate(ticket.getPurchaseDate())
                .ticketStatus(ticket.getStatus().toString())
                .build();
    }
}
