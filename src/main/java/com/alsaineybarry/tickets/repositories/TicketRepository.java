package com.alsaineybarry.tickets.repositories;

import com.alsaineybarry.tickets.domain.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    int countByTicketTypeId(UUID ticketTypeId);

    @Query("SELECT t FROM Ticket t WHERE t.purchaser.id = :purchaserId")
    Page<Ticket> findByPurchaserId(@Param("purchaserId") UUID purchaserId, Pageable pageable);

    @Query("SELECT t FROM Ticket t WHERE t.id = :id AND t.purchaser.id = :purchaserId")
    Optional<Ticket> findByIdAndPurchaserId(@Param("id") UUID id, @Param("purchaserId") UUID purchaserId);

    @Query("SELECT t FROM Ticket t WHERE t.ticketType.event.organizer.id = :organizerId")
    Page<Ticket> findByEventOrganizerId(@Param("organizerId") UUID organizerId, Pageable pageable);

    @Query("SELECT t FROM Ticket t WHERE t.ticketType.event.organizer.id = :organizerId AND t.ticketType.event.id = :eventId")
    Page<Ticket> findByEventOrganizerIdAndEventId(@Param("organizerId") UUID organizerId, @Param("eventId") UUID eventId, Pageable pageable);

    @Query("SELECT t FROM Ticket t WHERE t.ticketType.event.id = :eventId")
    List<Ticket> findByTicketTypeEventId(@Param("eventId") UUID eventId);
}
