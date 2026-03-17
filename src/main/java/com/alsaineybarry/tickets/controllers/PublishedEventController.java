package com.alsaineybarry.tickets.controllers;

import java.util.UUID;

import com.alsaineybarry.tickets.domain.dtos.GetPublishedEventDetailsResponseDto;
import com.alsaineybarry.tickets.domain.dtos.ListPublishedEventResponseDto;
import com.alsaineybarry.tickets.domain.entities.Event;
import com.alsaineybarry.tickets.mappers.EventMapper;
import com.alsaineybarry.tickets.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api/v1/published-events")
@RequiredArgsConstructor
@Tag(name = "Published Events", description = "Public APIs for browsing published events")
public class PublishedEventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    @Operation(summary = "List published events", description = "Retrieves a paginated list of published events with optional search functionality")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Events retrieved successfully")
    })
    public ResponseEntity<Page<ListPublishedEventResponseDto>> listPublishedEvents(
            @Parameter(description = "Search query for filtering events") @RequestParam(required = false) String q,
            Pageable pageable) {

        Page<Event> events;
        if (null != q && !q.trim().isEmpty()) {
            events = eventService.searchPublishedEvents(q, pageable);
        } else {
            events = eventService.listPublishedEvents(pageable);
        }

        return ResponseEntity.ok(
                events.map(eventMapper::toListPublishedEventResponseDto)
        );
    }

    @GetMapping(path = "/{eventId}")
    @Operation(summary = "Get published event details", description = "Retrieves detailed information about a specific published event")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Event details retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Event not found")
    })
    public ResponseEntity<GetPublishedEventDetailsResponseDto> getPublishedEventDetails(
            @Parameter(description = "Event ID", required = true) @PathVariable UUID eventId
    ) {
        return eventService.getPublishedEvent(eventId)
                .map(eventMapper::toGetPublishedEventDetailsResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}