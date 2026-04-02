package com.alsaineybarry.tickets.repositories;

import com.alsaineybarry.tickets.domain.entities.TicketValidation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TicketValidationRepository extends JpaRepository<TicketValidation, UUID> {
    @Query("SELECT tv FROM TicketValidation tv WHERE tv.ticket.ticketType.event.organizer.id = :organizerId AND tv.ticket.ticketType.event.id = :eventId")
    Page<TicketValidation> findByTicketEventOrganizerIdAndTicketEventId(
            @Param("organizerId") UUID organizerId, @Param("eventId") UUID eventId, Pageable pageable);
}
