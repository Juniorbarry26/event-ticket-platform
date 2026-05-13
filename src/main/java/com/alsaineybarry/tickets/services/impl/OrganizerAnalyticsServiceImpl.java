package com.alsaineybarry.tickets.services.impl;

import com.alsaineybarry.tickets.domain.dtos.*;
import com.alsaineybarry.tickets.domain.entities.*;
import com.alsaineybarry.tickets.domain.enums.EventStatusEnum;
import com.alsaineybarry.tickets.exceptions.EventNotFoundException;
import com.alsaineybarry.tickets.repositories.EventRepository;
import com.alsaineybarry.tickets.repositories.TicketRepository;
import com.alsaineybarry.tickets.repositories.TicketTypeRepository;
import com.alsaineybarry.tickets.repositories.UserRepository;
import com.alsaineybarry.tickets.services.OrganizerAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizerAnalyticsServiceImpl implements OrganizerAnalyticsService {

    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final UserRepository userRepository;

    @Override
    public OrganizerAnalyticsDto getEventAnalytics(UUID eventId, UUID organizerId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));

        // Verify organizer owns this event
        if (!event.getOrganizer().getId().equals(organizerId)) {
            throw new IllegalArgumentException("Unauthorized: You don't own this event");
        }

        List<TicketType> ticketTypes = ticketTypeRepository.findByEventId(eventId);
        List<Ticket> allTickets = ticketRepository.findByTicketTypeEventId(eventId);

        // Calculate metrics
        BigDecimal totalRevenue = calculateTotalRevenue(allTickets);
        BigDecimal totalPotentialRevenue = calculateTotalPotentialRevenue(ticketTypes);
        BigDecimal revenueLoss = totalPotentialRevenue.subtract(totalRevenue);
        BigDecimal netProfit = totalRevenue; // For now, profit = revenue (can add costs later)

        Integer totalTicketsSold = allTickets.size();
        Integer totalTicketsAvailable = ticketTypes.stream()
                .mapToInt(TicketType::getTotalAvailable)
                .sum();
        Integer ticketsRemaining = totalTicketsAvailable - totalTicketsSold;
        Double salesPercentage = totalTicketsAvailable > 0 ? 
                (totalTicketsSold.doubleValue() / totalTicketsAvailable) * 100 : 0.0;

        // Get ticket purchasers
        List<TicketPurchaserDto> ticketPurchasers = getTicketPurchasers(allTickets);

        // Get ticket type sales
        List<TicketTypeSalesDto> ticketTypeSales = getTicketTypeSales(ticketTypes, allTickets);

        // Calculate price metrics
        Optional<BigDecimal> highestPrice = ticketTypes.stream()
                .map(TicketType::getPrice)
                .map(BigDecimal::valueOf)
                .max((a, b) -> a.compareTo(b));
        Optional<BigDecimal> lowestPrice = ticketTypes.stream()
                .map(TicketType::getPrice)
                .map(BigDecimal::valueOf)
                .min((a, b) -> a.compareTo(b));
        BigDecimal averageTicketPrice = totalTicketsSold > 0 ?
                totalRevenue.divide(BigDecimal.valueOf(totalTicketsSold), 2, RoundingMode.HALF_UP) :
                BigDecimal.ZERO;

        return OrganizerAnalyticsDto.builder()
                .eventId(event.getId())
                .eventName(event.getName())
                .eventStart(event.getStart())
                .eventEnd(event.getEnd())
                .venue(event.getVenue())
                .totalRevenue(totalRevenue)
                .totalPotentialRevenue(totalPotentialRevenue)
                .revenueLoss(revenueLoss)
                .netProfit(netProfit)
                .totalTicketsSold(totalTicketsSold)
                .totalTicketsAvailable(totalTicketsAvailable)
                .ticketsRemaining(ticketsRemaining)
                .salesPercentage(salesPercentage)
                .averageTicketPrice(averageTicketPrice)
                .highestTicketPrice(highestPrice.orElse(BigDecimal.ZERO))
                .lowestTicketPrice(lowestPrice.orElse(BigDecimal.ZERO))
                .ticketPurchasers(ticketPurchasers)
                .ticketTypeSales(ticketTypeSales)
                .build();
    }

    @Override
    public OrganizerDashboardDto getOrganizerDashboard(UUID organizerId) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new IllegalArgumentException("Organizer not found"));

        List<Event> organizerEvents = eventRepository.findByOrganizerId(organizerId);
        LocalDateTime now = LocalDateTime.now();

        // Categorize events
        List<Event> activeEvents = organizerEvents.stream()
                .filter(e -> e.getStatus() == EventStatusEnum.PUBLISHED)
                .collect(Collectors.toList());
        List<Event> pastEvents = organizerEvents.stream()
                .filter(e -> e.getEnd().isBefore(now))
                .collect(Collectors.toList());
        List<Event> upcomingEvents = organizerEvents.stream()
                .filter(e -> e.getStart().isAfter(now))
                .collect(Collectors.toList());

        // Get all tickets for organizer's events
        List<Ticket> allTickets = organizerEvents.stream()
                .flatMap(event -> ticketRepository.findByTicketTypeEventId(event.getId()).stream())
                .collect(Collectors.toList());

        // Calculate overall metrics
        BigDecimal totalRevenueAllEvents = calculateTotalRevenue(allTickets);
        BigDecimal totalPotentialRevenueAllEvents = calculateTotalPotentialRevenueForEvents(organizerEvents);
        BigDecimal totalRevenueLossAllEvents = totalPotentialRevenueAllEvents.subtract(totalRevenueAllEvents);
        BigDecimal totalNetProfitAllEvents = totalRevenueAllEvents; // Simplified for now

        Integer totalTicketsSoldAllEvents = allTickets.size();
        Integer totalTicketsAvailableAllEvents = organizerEvents.stream()
                .flatMap(event -> ticketTypeRepository.findByEventId(event.getId()).stream())
                .mapToInt(TicketType::getTotalAvailable)
                .sum();
        Double overallSalesPercentage = totalTicketsAvailableAllEvents > 0 ?
                (totalTicketsSoldAllEvents.doubleValue() / totalTicketsAvailableAllEvents) * 100 : 0.0;

        // Get recent activities (simplified - last 10 ticket purchases)
        List<RecentActivityDto> recentActivities = allTickets.stream()
                .sorted(Comparator.comparing(Ticket::getPurchaseDate).reversed())
                .limit(10)
                .map(ticket -> RecentActivityDto.builder()
                        .activityType("TICKET_SOLD")
                        .description(ticket.getPurchaser().getName() + " purchased ticket for " + 
                                ticket.getTicketType().getEvent().getName())
                        .timestamp(ticket.getPurchaseDate())
                        .eventName(ticket.getTicketType().getEvent().getName())
                        .purchaserName(ticket.getPurchaser().getName())
                        .amount(BigDecimal.valueOf(ticket.getTicketType().getPrice()))
                        .ticketCount(1)
                        .build())
                .collect(Collectors.toList());

        // Get event summaries
        List<EventSummaryDto> eventSummaries = organizerEvents.stream()
                .map(event -> {
                    List<Ticket> eventTickets = ticketRepository.findByTicketTypeEventId(event.getId());
                    List<TicketType> eventTicketTypes = ticketTypeRepository.findByEventId(event.getId());
                    
                    BigDecimal eventRevenue = calculateTotalRevenue(eventTickets);
                    BigDecimal eventPotentialRevenue = calculateTotalPotentialRevenue(eventTicketTypes);
                    BigDecimal eventRevenueLoss = eventPotentialRevenue.subtract(eventRevenue);
                    
                    Integer eventTicketsSold = eventTickets.size();
                    Integer eventTicketsAvailable = eventTicketTypes.stream()
                            .mapToInt(TicketType::getTotalAvailable)
                            .sum();
                    Double eventSalesPercentage = eventTicketsAvailable > 0 ?
                            (eventTicketsSold.doubleValue() / eventTicketsAvailable) * 100 : 0.0;
                    
                    BigDecimal eventNetProfit = eventRevenue; // Simplified
                    Double eventProfitMargin = eventPotentialRevenue.compareTo(BigDecimal.ZERO) > 0 ?
                            eventNetProfit.divide(eventPotentialRevenue, 4, RoundingMode.HALF_UP)
                                    .multiply(BigDecimal.valueOf(100)).doubleValue() : 0.0;

                    return EventSummaryDto.builder()
                            .eventId(event.getId())
                            .eventName(event.getName())
                            .eventStart(event.getStart())
                            .eventEnd(event.getEnd())
                            .venue(event.getVenue())
                            .status(event.getStatus().name())
                            .totalRevenue(eventRevenue)
                            .totalPotentialRevenue(eventPotentialRevenue)
                            .revenueLoss(eventRevenueLoss)
                            .totalTicketsSold(eventTicketsSold)
                            .totalTicketsAvailable(eventTicketsAvailable)
                            .salesPercentage(eventSalesPercentage)
                            .netProfit(eventNetProfit)
                            .profitMargin(eventProfitMargin)
                            .build();
                })
                .collect(Collectors.toList());

        return OrganizerDashboardDto.builder()
                .organizerId(organizer.getId())
                .organizerName(organizer.getName())
                .organizerEmail(organizer.getEmail())
                .totalEvents(organizerEvents.size())
                .activeEvents(activeEvents.size())
                .pastEvents(pastEvents.size())
                .upcomingEvents(upcomingEvents.size())
                .totalRevenueAllEvents(totalRevenueAllEvents)
                .totalPotentialRevenueAllEvents(totalPotentialRevenueAllEvents)
                .totalRevenueLossAllEvents(totalRevenueLossAllEvents)
                .totalNetProfitAllEvents(totalNetProfitAllEvents)
                .totalTicketsSoldAllEvents(totalTicketsSoldAllEvents)
                .totalTicketsAvailableAllEvents(totalTicketsAvailableAllEvents)
                .overallSalesPercentage(overallSalesPercentage)
                .recentActivities(recentActivities)
                .eventSummaries(eventSummaries)
                .build();
    }

    @Override
    public Page<TicketPurchaserDto> getEventTicketPurchasers(UUID eventId, UUID organizerId, Pageable pageable) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));

        if (!event.getOrganizer().getId().equals(organizerId)) {
            throw new IllegalArgumentException("Unauthorized: You don't own this event");
        }

        List<Ticket> tickets = ticketRepository.findByTicketTypeEventId(eventId);
        List<TicketPurchaserDto> purchasers = getTicketPurchasers(tickets);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), purchasers.size());
        List<TicketPurchaserDto> pageContent = purchasers.subList(start, end);

        return new PageImpl<>(pageContent, pageable, purchasers.size());
    }

    @Override
    public Page<TicketTypeSalesDto> getEventTicketTypeSales(UUID eventId, UUID organizerId, Pageable pageable) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));

        if (!event.getOrganizer().getId().equals(organizerId)) {
            throw new IllegalArgumentException("Unauthorized: You don't own this event");
        }

        List<TicketType> ticketTypes = ticketTypeRepository.findByEventId(eventId);
        List<Ticket> allTickets = ticketRepository.findByTicketTypeEventId(eventId);
        List<TicketTypeSalesDto> salesData = getTicketTypeSales(ticketTypes, allTickets);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), salesData.size());
        List<TicketTypeSalesDto> pageContent = salesData.subList(start, end);

        return new PageImpl<>(pageContent, pageable, salesData.size());
    }

    @Override
    public RevenueSummaryDto getOrganizerRevenueSummary(UUID organizerId) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new IllegalArgumentException("Organizer not found"));

        List<Event> organizerEvents = eventRepository.findByOrganizerId(organizerId);
        List<Ticket> allTickets = organizerEvents.stream()
                .flatMap(event -> ticketRepository.findByTicketTypeEventId(event.getId()).stream())
                .collect(Collectors.toList());

        BigDecimal totalRevenue = calculateTotalRevenue(allTickets);
        BigDecimal totalPotentialRevenue = calculateTotalPotentialRevenueForEvents(organizerEvents);
        BigDecimal revenueLoss = totalPotentialRevenue.subtract(totalRevenue);
        BigDecimal netProfit = totalRevenue; // Simplified

        // Revenue by event
        List<EventRevenueDto> eventRevenues = organizerEvents.stream()
                .map(event -> {
                    List<Ticket> eventTickets = ticketRepository.findByTicketTypeEventId(event.getId());
                    List<TicketType> eventTicketTypes = ticketTypeRepository.findByEventId(event.getId());
                    
                    BigDecimal eventRevenue = calculateTotalRevenue(eventTickets);
                    BigDecimal eventPotentialRevenue = calculateTotalPotentialRevenue(eventTicketTypes);
                    BigDecimal eventRevenueLoss = eventPotentialRevenue.subtract(eventRevenue);
                    
                    Integer eventTicketsSold = eventTickets.size();
                    Integer eventTicketsAvailable = eventTicketTypes.stream()
                            .mapToInt(TicketType::getTotalAvailable)
                            .sum();
                    Double eventSalesPercentage = eventTicketsAvailable > 0 ?
                            (eventTicketsSold.doubleValue() / eventTicketsAvailable) * 100 : 0.0;

                    return EventRevenueDto.builder()
                            .eventId(event.getId())
                            .eventName(event.getName())
                            .eventStart(event.getStart())
                            .venue(event.getVenue())
                            .revenue(eventRevenue)
                            .potentialRevenue(eventPotentialRevenue)
                            .revenueLoss(eventRevenueLoss)
                            .profit(eventRevenue) // Simplified
                            .ticketsSold(eventTicketsSold)
                            .ticketsAvailable(eventTicketsAvailable)
                            .salesPercentage(eventSalesPercentage)
                            .build();
                })
                .collect(Collectors.toList());

        // Calculate additional metrics
        BigDecimal averageRevenuePerEvent = organizerEvents.size() > 0 ?
                totalRevenue.divide(BigDecimal.valueOf(organizerEvents.size()), 2, RoundingMode.HALF_UP) :
                BigDecimal.ZERO;
        
        Optional<BigDecimal> highestRevenue = eventRevenues.stream()
                .map(EventRevenueDto::getRevenue)
                .max(BigDecimal::compareTo);
        Optional<BigDecimal> lowestRevenue = eventRevenues.stream()
                .map(EventRevenueDto::getRevenue)
                .min(BigDecimal::compareTo);

        return RevenueSummaryDto.builder()
                .organizerId(organizer.getId())
                .organizerName(organizer.getName())
                .totalRevenue(totalRevenue)
                .totalPotentialRevenue(totalPotentialRevenue)
                .revenueLoss(revenueLoss)
                .netProfit(netProfit)
                .eventRevenues(eventRevenues)
                .averageRevenuePerEvent(averageRevenuePerEvent)
                .highestRevenueEvent(highestRevenue.orElse(BigDecimal.ZERO))
                .lowestRevenueEvent(lowestRevenue.orElse(BigDecimal.ZERO))
                .build();
    }

    @Override
    public ProfitLossAnalysisDto getEventProfitLossAnalysis(UUID eventId, UUID organizerId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));

        if (!event.getOrganizer().getId().equals(organizerId)) {
            throw new IllegalArgumentException("Unauthorized: You don't own this event");
        }

        List<TicketType> ticketTypes = ticketTypeRepository.findByEventId(eventId);
        List<Ticket> allTickets = ticketRepository.findByTicketTypeEventId(eventId);

        BigDecimal totalRevenue = calculateTotalRevenue(allTickets);
        BigDecimal totalPotentialRevenue = calculateTotalPotentialRevenue(ticketTypes);
        BigDecimal totalLoss = totalPotentialRevenue.subtract(totalRevenue);

        // Simplified profit/loss (revenue = profit for now)
        BigDecimal grossProfit = totalRevenue;
        BigDecimal netProfit = totalRevenue; // Can subtract costs later
        BigDecimal profitMargin = totalPotentialRevenue.compareTo(BigDecimal.ZERO) > 0 ?
                netProfit.divide(totalPotentialRevenue, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)) : BigDecimal.ZERO;

        // Ticket type breakdown
        List<TicketTypeProfitLossDto> ticketTypeBreakdown = ticketTypes.stream()
                .map(ticketType -> {
                    List<Ticket> typeTickets = allTickets.stream()
                            .filter(ticket -> ticket.getTicketType().getId().equals(ticketType.getId()))
                            .collect(Collectors.toList());

                    BigDecimal typeRevenue = calculateTotalRevenue(typeTickets);
                    BigDecimal typePotentialRevenue = BigDecimal.valueOf(ticketType.getPrice())
                            .multiply(BigDecimal.valueOf(ticketType.getTotalAvailable()));
                    BigDecimal typeRevenueLoss = typePotentialRevenue.subtract(typeRevenue);
                    BigDecimal typeProfit = typeRevenue; // Simplified
                    BigDecimal typeProfitMargin = typePotentialRevenue.compareTo(BigDecimal.ZERO) > 0 ?
                            typeProfit.divide(typePotentialRevenue, 4, RoundingMode.HALF_UP)
                                    .multiply(BigDecimal.valueOf(100)) : BigDecimal.ZERO;

                    Integer typeSold = typeTickets.size();
                    Double typeSalesPercentage = ticketType.getTotalAvailable() > 0 ?
                            (typeSold.doubleValue() / ticketType.getTotalAvailable()) * 100 : 0.0;

                    return TicketTypeProfitLossDto.builder()
                            .ticketTypeId(ticketType.getId())
                            .ticketTypeName(ticketType.getName())
                            .price(BigDecimal.valueOf(ticketType.getPrice()))
                            .totalAvailable(ticketType.getTotalAvailable())
                            .totalSold(typeSold)
                            .remaining(ticketType.getTotalAvailable() - typeSold)
                            .revenue(typeRevenue)
                            .potentialRevenue(typePotentialRevenue)
                            .revenueLoss(typeRevenueLoss)
                            .profit(typeProfit)
                            .profitMargin(typeProfitMargin)
                            .salesPercentage(typeSalesPercentage)
                            .build();
                })
                .collect(Collectors.toList());

        // Break-even analysis (simplified)
        Double breakEvenPercentage = totalPotentialRevenue.compareTo(BigDecimal.ZERO) > 0 ?
                totalRevenue.divide(totalPotentialRevenue, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)).doubleValue() : 0.0;
        
        Integer ticketsNeededToBreakEven = 0; // Simplified - would need cost data

        return ProfitLossAnalysisDto.builder()
                .eventId(event.getId())
                .eventName(event.getName())
                .totalRevenue(totalRevenue)
                .ticketRevenue(totalRevenue)
                .otherRevenue(BigDecimal.ZERO)
                .totalCosts(BigDecimal.ZERO) // Can add actual costs later
                .platformFees(BigDecimal.ZERO)
                .processingFees(BigDecimal.ZERO)
                .otherCosts(BigDecimal.ZERO)
                .grossProfit(grossProfit)
                .netProfit(netProfit)
                .profitMargin(profitMargin)
                .totalLoss(totalLoss)
                .unsoldTicketsLoss(totalLoss)
                .otherLosses(BigDecimal.ZERO)
                .ticketTypeBreakdown(ticketTypeBreakdown)
                .breakEvenPercentage(breakEvenPercentage)
                .ticketsNeededToBreakEven(ticketsNeededToBreakEven)
                .build();
    }

    // Helper methods
    private BigDecimal calculateTotalRevenue(List<Ticket> tickets) {
        return tickets.stream()
                .map(ticket -> BigDecimal.valueOf(ticket.getTicketType().getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotalPotentialRevenue(List<TicketType> ticketTypes) {
        return ticketTypes.stream()
                .map(type -> BigDecimal.valueOf(type.getPrice()).multiply(BigDecimal.valueOf(type.getTotalAvailable())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotalPotentialRevenueForEvents(List<Event> events) {
        return events.stream()
                .flatMap(event -> ticketTypeRepository.findByEventId(event.getId()).stream())
                .map(type -> BigDecimal.valueOf(type.getPrice()).multiply(BigDecimal.valueOf(type.getTotalAvailable())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<TicketPurchaserDto> getTicketPurchasers(List<Ticket> tickets) {
        return tickets.stream()
                .collect(Collectors.groupingBy(
                        Ticket::getPurchaser,
                        Collectors.toList()
                ))
                .entrySet().stream()
                .map(entry -> {
                    User purchaser = entry.getKey();
                    List<Ticket> purchaserTickets = entry.getValue();
                    
                    BigDecimal totalAmountPaid = purchaserTickets.stream()
                            .map(ticket -> BigDecimal.valueOf(ticket.getTicketType().getPrice()))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    List<String> ticketTypeNames = purchaserTickets.stream()
                            .map(ticket -> ticket.getTicketType().getName())
                            .distinct()
                            .collect(Collectors.toList());

                    return TicketPurchaserDto.builder()
                            .purchaserId(purchaser.getId())
                            .purchaserName(purchaser.getName())
                            .purchaserEmail(purchaser.getEmail())
                            .purchaseDate(purchaserTickets.get(0).getPurchaseDate())
                            .ticketsPurchased(purchaserTickets.size())
                            .totalAmountPaid(totalAmountPaid)
                            .ticketTypeNames(ticketTypeNames)
                            .status("ACTIVE")
                            .build();
                })
                .sorted(Comparator.comparing(TicketPurchaserDto::getPurchaseDate).reversed())
                .collect(Collectors.toList());
    }

    private List<TicketTypeSalesDto> getTicketTypeSales(List<TicketType> ticketTypes, List<Ticket> allTickets) {
        return ticketTypes.stream()
                .map(ticketType -> {
                    List<Ticket> typeTickets = allTickets.stream()
                            .filter(ticket -> ticket.getTicketType().getId().equals(ticketType.getId()))
                            .collect(Collectors.toList());

                    BigDecimal revenue = calculateTotalRevenue(typeTickets);
                    BigDecimal potentialRevenue = BigDecimal.valueOf(ticketType.getPrice())
                            .multiply(BigDecimal.valueOf(ticketType.getTotalAvailable()));
                    BigDecimal revenueLoss = potentialRevenue.subtract(revenue);

                    Integer sold = typeTickets.size();
                    Integer remaining = ticketType.getTotalAvailable() - sold;
                    Double salesPercentage = ticketType.getTotalAvailable() > 0 ?
                            (sold.doubleValue() / ticketType.getTotalAvailable()) * 100 : 0.0;

                    List<TicketPurchaserDto> purchasers = getTicketPurchasers(typeTickets);

                    return TicketTypeSalesDto.builder()
                            .ticketTypeId(ticketType.getId())
                            .ticketTypeName(ticketType.getName())
                            .description(ticketType.getDescription())
                            .price(BigDecimal.valueOf(ticketType.getPrice()))
                            .totalAvailable(ticketType.getTotalAvailable())
                            .totalSold(sold)
                            .remaining(remaining)
                            .revenue(revenue)
                            .salesPercentage(salesPercentage)
                            .purchasers(purchasers)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
