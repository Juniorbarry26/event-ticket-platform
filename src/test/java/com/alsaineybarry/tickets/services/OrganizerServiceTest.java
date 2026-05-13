package com.alsaineybarry.tickets.services;

import com.alsaineybarry.tickets.domain.dtos.OrganizerOverallProfitDto;
import com.alsaineybarry.tickets.domain.dtos.OrganizerProfitSummaryDto;
import com.alsaineybarry.tickets.domain.dtos.OrganizerTicketPurchaseDto;
import com.alsaineybarry.tickets.domain.entities.*;
import com.alsaineybarry.tickets.domain.enums.EventStatusEnum;
import com.alsaineybarry.tickets.domain.enums.RoleEnum;
import com.alsaineybarry.tickets.domain.enums.TIcketStatusEnum;
import com.alsaineybarry.tickets.repositories.EventRepository;
import com.alsaineybarry.tickets.repositories.TicketRepository;
import com.alsaineybarry.tickets.repositories.TicketTypeRepository;
import com.alsaineybarry.tickets.services.impl.OrganizerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.mockito.ArgumentMatchers.eq;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizerServiceTest {

    @Mock
    private TicketRepository ticketRepository;
    
    @Mock
    private EventRepository eventRepository;
    
    @Mock
    private TicketTypeRepository ticketTypeRepository;
    
    private OrganizerService organizerService;
    
    private UUID organizerId;
    private UUID eventId;
    private UUID ticketId;
    private UUID ticketTypeId;
    private UUID purchaserId;
    
    private User organizer;
    private User purchaser;
    private Event event;
    private TicketType ticketType;
    private Ticket ticket;
    
    @BeforeEach
    void setUp() {
        organizerService = new OrganizerServiceImpl(ticketRepository, eventRepository, ticketTypeRepository);
        
        organizerId = UUID.randomUUID();
        eventId = UUID.randomUUID();
        ticketId = UUID.randomUUID();
        ticketTypeId = UUID.randomUUID();
        purchaserId = UUID.randomUUID();
        
        organizer = User.builder()
                .id(organizerId)
                .name("Organizer")
                .email("organizer@example.com")
                .build();
        
        purchaser = User.builder()
                .id(purchaserId)
                .name("Purchaser")
                .email("purchaser@example.com")
                .build();
        
        event = Event.builder()
                .id(eventId)
                .name("Test Event")
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .organizer(organizer)
                .status(EventStatusEnum.PUBLISHED)
                .build();
        
        ticketType = new TicketType();
        ticketType.setId(ticketTypeId);
        ticketType.setName("VIP Ticket");
        ticketType.setPrice(100.0);
        ticketType.setTotalAvailable(100);
        ticketType.setEvent(event);
        
        ticket = Ticket.builder()
                .id(ticketId)
                .status(TIcketStatusEnum.PURCHASED)
                .ticketType(ticketType)
                .purchaser(purchaser)
                .purchaseDate(LocalDateTime.now())
                .build();
    }
    
    @Test
    void getTicketPurchases_ShouldReturnTicketPurchasesForOrganizer() {
        // Given
        Page<Ticket> tickets = new PageImpl<>(List.of(ticket));
        when(ticketRepository.findByEventOrganizerId(eq(organizerId), any(Pageable.class)))
                .thenReturn(tickets);
        
        // When
        Page<OrganizerTicketPurchaseDto> result = organizerService.getTicketPurchases(organizerId, Pageable.unpaged());
        
        // Then
        assertEquals(1, result.getContent().size());
        OrganizerTicketPurchaseDto purchaseDto = result.getContent().get(0);
        assertEquals(ticketId, purchaseDto.getTicketId());
        assertEquals("VIP Ticket", purchaseDto.getTicketTypeName());
        assertEquals("Test Event", purchaseDto.getEventName());
        assertEquals("Purchaser", purchaseDto.getPurchaserName());
        assertEquals("purchaser@example.com", purchaseDto.getPurchaserEmail());
        assertEquals(100.0, purchaseDto.getTicketPrice());
        assertEquals("PURCHASED", purchaseDto.getTicketStatus());
    }
    
    @Test
    void getProfitSummaryForEvent_ShouldReturnProfitSummary() {
        // Given
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(ticketRepository.findByEventOrganizerIdAndEventId(eq(organizerId), eq(eventId), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(ticket)));
        
        // When
        OrganizerProfitSummaryDto result = organizerService.getProfitSummaryForEvent(organizerId, eventId);
        
        // Then
        assertEquals(eventId, result.getEventId());
        assertEquals("Test Event", result.getEventName());
        assertEquals(1, result.getTotalTicketsSold());
        assertEquals(100.0, result.getTotalRevenue());
        assertEquals(100.0, result.getTotalProfit());
        assertEquals(1, result.getTicketTypeProfits().size());
        
        OrganizerProfitSummaryDto.TicketTypeProfitDto ticketTypeProfit = result.getTicketTypeProfits().get(0);
        assertEquals(ticketTypeId, ticketTypeProfit.getTicketTypeId());
        assertEquals("VIP Ticket", ticketTypeProfit.getTicketTypeName());
        assertEquals(100.0, ticketTypeProfit.getTicketPrice());
        assertEquals(1, ticketTypeProfit.getTicketsSold());
        assertEquals(100.0, ticketTypeProfit.getRevenue());
        assertEquals(100.0, ticketTypeProfit.getProfit());
    }
    
    @Test
    void getProfitSummaryForEvent_ShouldThrowException_WhenEventNotFound() {
        // Given
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            organizerService.getProfitSummaryForEvent(organizerId, eventId);
        });
    }
    
    @Test
    void getProfitSummaryForEvent_ShouldThrowException_WhenEventNotBelongingToOrganizer() {
        // Given
        User differentOrganizer = User.builder()
                .id(UUID.randomUUID())
                .name("Different Organizer")
                .build();
        
        Event differentEvent = Event.builder()
                .id(eventId)
                .name("Different Event")
                .organizer(differentOrganizer)
                .build();
        
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(differentEvent));
        
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            organizerService.getProfitSummaryForEvent(organizerId, eventId);
        });
    }
    
    @Test
    void getOverallProfitSummary_ShouldReturnOverallProfitSummary() {
        // Given
        when(ticketRepository.findByEventOrganizerId(eq(organizerId), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(ticket)));
        when(ticketRepository.findByEventOrganizerIdAndEventId(eq(organizerId), eq(eventId), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(ticket)));
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        
        // When
        OrganizerOverallProfitDto result = organizerService.getOverallProfitSummary(organizerId);
        
        // Then
        assertEquals(1, result.getTotalEvents());
        assertEquals(1, result.getTotalTicketsSold());
        assertEquals(100.0, result.getTotalRevenue());
        assertEquals(100.0, result.getTotalProfit());
        assertEquals(1, result.getEventProfits().size());
    }
}
