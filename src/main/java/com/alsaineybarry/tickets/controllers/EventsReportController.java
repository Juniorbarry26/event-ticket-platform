package com.alsaineybarry.tickets.controllers;

import com.alsaineybarry.tickets.domain.dtos.AdminReportDto;
import com.alsaineybarry.tickets.domain.entities.Event;
import com.alsaineybarry.tickets.domain.entities.Ticket;
import com.alsaineybarry.tickets.repositories.EventRepository;
import com.alsaineybarry.tickets.repositories.TicketRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/reports")
@RequiredArgsConstructor
@Tag(name = "Admin Reports", description = "Endpoints for admin reports")
public class EventsReportController {

    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

    @Operation(
            summary = "Get events report",
            description = "Retrieves a comprehensive events report with statistics and performance data"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Events report retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/events-summary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminReportDto.EventReportDto> getEventsReport() {
        try {
            AdminReportDto.EventReportDto eventReport = generateEventReport();
            return ResponseEntity.ok(eventReport);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private AdminReportDto.EventReportDto generateEventReport() {
        // Get all events for statistics
        List<Event> allEvents = eventRepository.findAll();
        
        int totalEvents = allEvents.size();
        int activeEvents = (int) allEvents.stream().filter(event -> 
                event.getStart().isAfter(LocalDateTime.now())).count();
        int completedEvents = (int) allEvents.stream().filter(event -> 
                event.getStart().isBefore(LocalDateTime.now())).count();
        int cancelledEvents = (int) allEvents.stream().filter(event -> 
                "CANCELLED".equals(event.getStatus())).count();
        
        // Calculate total attendees and revenue
        int totalAttendees = 0;
        double totalRevenue = 0.0;
        List<AdminReportDto.EventReportDto.EventPerformanceDto> eventPerformances = new ArrayList<>();
        
        for (Event event : allEvents) {
            List<Ticket> tickets = ticketRepository.findByTicketTypeEventId(event.getId());
            int ticketsSold = tickets.size();
            double revenue = tickets.stream()
                    .mapToDouble(ticket -> ticket.getTicketType().getPrice())
                    .sum();
            int attendeeCount = (int) tickets.stream()
                    .filter(ticket -> ticket.isCheckedIn())
                    .count();
            
            totalAttendees += attendeeCount;
            totalRevenue += revenue;
            
            AdminReportDto.EventReportDto.EventPerformanceDto performance = 
                    AdminReportDto.EventReportDto.EventPerformanceDto.builder()
                            .eventTitle(event.getName())
                            .organizerName(event.getOrganizer() != null ? event.getOrganizer().getName() : "Unknown")
                            .ticketsSold(ticketsSold)
                            .revenue(BigDecimal.valueOf(revenue))
                            .attendeeCount(attendeeCount)
                            .status(event.getStatus().toString())
                            .build();
            
            eventPerformances.add(performance);
        }
        
        return AdminReportDto.EventReportDto.builder()
                .totalEvents(totalEvents)
                .activeEvents(activeEvents)
                .completedEvents(completedEvents)
                .cancelledEvents(cancelledEvents)
                .totalAttendees(totalAttendees)
                .totalRevenue(BigDecimal.valueOf(totalRevenue))
                .eventPerformances(eventPerformances)
                .build();
    }
}
