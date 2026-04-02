package com.alsaineybarry.tickets.controllers;

import com.alsaineybarry.tickets.domain.dtos.GetTicketResponseDto;
import com.alsaineybarry.tickets.domain.dtos.ListTicketResponseDto;
import com.alsaineybarry.tickets.domain.entities.User;
import com.alsaineybarry.tickets.mappers.TicketMapper;
import com.alsaineybarry.tickets.services.QrCodeService;
import com.alsaineybarry.tickets.services.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Tag(name = "Ticket Management", description = "Endpoints for managing user tickets")
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    private final QrCodeService qrCodeService;

    @Operation(
            summary = "Get all tickets of the authenticated user",
            description = "Retrieves a paginated list of tickets for the authenticated user"
    )
    @GetMapping
    public Page<ListTicketResponseDto> getUserTickets(
            Pageable pageable,
            @AuthenticationPrincipal User user) {
        return ticketService.listTicketsForUser(
                user.getId(),
                pageable
        ).map(ticketMapper::toListTicketResponseDto);
    }

    @Operation(
            summary = "Get a ticket by its ID",
            description = "Retrieves detailed information about a specific ticket for the authenticated user"
    )
    @GetMapping("/{ticketId}")
    public ResponseEntity<GetTicketResponseDto> getTicketById(
            @PathVariable UUID ticketId,
            @AuthenticationPrincipal User user) {
        return ticketService
                .getTicketForUser(user.getId(), ticketId)
                .map(ticketMapper::toGetTicketResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Get ticket QR code",
            description = "Generates and returns the QR code image for a specific ticket"
    )
    @GetMapping("/{ticketId}/qr-codes")
    public ResponseEntity<byte[]> getTicketQrCode(
            @PathVariable UUID ticketId,
            @AuthenticationPrincipal User user) {
        
        byte[] qrCodeImage = qrCodeService.getQrCodeImageForUserAndTicket(
                user.getId(),
                ticketId
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(qrCodeImage.length);
        headers.setCacheControl(CacheControl.maxAge(Duration.ofHours(1)));

        return ResponseEntity.ok()
                .headers(headers)
                .body(qrCodeImage);
    }
}