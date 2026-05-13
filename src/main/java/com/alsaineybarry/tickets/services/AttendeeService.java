package com.alsaineybarry.tickets.services;

import com.alsaineybarry.tickets.domain.dtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AttendeeService {

    // Note: Public events endpoints are handled by PublishedEventController

    // Ticket Management (Attendee-specific)
    byte[] downloadTicketPdf(UUID ticketId);

    // Attendee Dashboard & Stats
    AttendeeStatsDto getAttendeeStats();
    Page<AttendeeEventDto> getUpcomingEvents(Pageable pageable);
    Page<AttendeeEventDto> getPastEvents(Pageable pageable);
    AttendeeEventDetailsDto getAttendeeEventDetails(UUID eventId);
    Page<AttendeeTicketDto> getAllAttendeeTickets(Pageable pageable);

    // Ticket Transfer
    TicketTransferDto initiateTicketTransfer(UUID ticketId, TicketTransferRequestDto transferRequest);
    Page<TicketTransferDto> getTicketTransferHistory(Pageable pageable);

    // Refunds
    RefundRequestDto requestRefund(UUID ticketId, RefundRequestDto refundRequest);
    Page<RefundRequestDto> getRefundRequests(Pageable pageable);
    EventRefundPolicyDto getEventRefundPolicy(UUID eventId);

    // User Profile
    AttendeeProfileDto getAttendeeProfile();
    AttendeeProfileDto updateAttendeeProfile(AttendeeProfileDto profile);
    NotificationPreferencesDto getNotificationPreferences();
    NotificationPreferencesDto updateNotificationPreferences(NotificationPreferencesDto preferences);

    // Note: Ticket validation endpoint is handled by TicketValidationController
}
