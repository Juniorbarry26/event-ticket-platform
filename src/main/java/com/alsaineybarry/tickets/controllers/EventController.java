package com.alsaineybarry.tickets.controllers;

import com.alsaineybarry.tickets.domain.dtos.*;
import com.alsaineybarry.tickets.domain.entities.Event;
import com.alsaineybarry.tickets.domain.entities.User;
import com.alsaineybarry.tickets.domain.requests.CreateEventRequest;
import com.alsaineybarry.tickets.domain.requests.UpdateEventRequest;
import com.alsaineybarry.tickets.mappers.EventMapper;
import com.alsaineybarry.tickets.services.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Tag(name = "Event Management", description = "Endpoints for event creation, updates, retrieval, and deletion")
public class EventController {

    private final EventMapper eventMapper;
    private final EventService eventService;

    @Operation(
            summary = "Add a new event for organizer",
            description = "Creates a new event for the authenticated organizer. Only organizers can create events."
    )
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    public ResponseEntity<CreateEventResponseDto> addEvent(
            @Valid @RequestBody CreateEventRequestDto createEventRequestDto,
            @AuthenticationPrincipal User user) {
        CreateEventRequest createEventRequest = eventMapper.fromDto(createEventRequestDto);
        Event createdEvent = eventService.createEvent(user.getId(), createEventRequest);
        CreateEventResponseDto createEventResponseDto = eventMapper.toDto(createdEvent);
        return ResponseEntity.ok(createEventResponseDto);
    }

    @Operation(
            summary = "Update an existing event",
            description = "Updates only the fields provided in the request. Event must belong to the authenticated organizer."
    )
    @PutMapping("/{eventId}")
    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    public ResponseEntity<UpdateEventResponseDto> updateEvent(
            @PathVariable UUID eventId,
            @Valid @RequestBody UpdateEventRequestDto updateEventRequestDto,
            @AuthenticationPrincipal User user) {
        UpdateEventRequest updateEventRequest = eventMapper.fromDto(updateEventRequestDto);
        Event updatedEvent = eventService.updateEventForOrganizer(
                user.getId(), eventId, updateEventRequest
        );
        UpdateEventResponseDto updateEventResponseDto = eventMapper.toUpdateEventResponseDto(updatedEvent);
        return ResponseEntity.ok(updateEventResponseDto);
    }

    @Operation(
            summary = "Get all events of the authenticated organizer",
            description = "Retrieves a paginated list of events for the authenticated organizer"
    )
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    public ResponseEntity<Page<ListEventResponseDto>> getOrganizerEvents(
            Pageable pageable,
            @AuthenticationPrincipal User user) {
        Page<Event> events = eventService.listEventsForOrganizer(user.getId(), pageable);
        return ResponseEntity.ok(events.map(eventMapper::toListEventResponseDto));
    }

    @Operation(
            summary = "Get an event by its ID",
            description = "Retrieves detailed information about a specific event for the authenticated organizer"
    )
    @GetMapping("/{eventId}")
    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    public ResponseEntity<GetEventDetailsResponseDto> getEventById(
            @PathVariable UUID eventId,
            @AuthenticationPrincipal User user) {
        return eventService.getEventForOrganizer(user.getId(), eventId)
                .map(eventMapper::toGetEventDetailsResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete an event",
            description = "Deletes an event only if it belongs to the authenticated organizer."
    )
    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasRole('ROLE_ORGANIZER')")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable UUID eventId,
            @AuthenticationPrincipal User user) {
        eventService.deleteEventForOrganizer(user.getId(), eventId);
        return ResponseEntity.noContent().build();
    }
}