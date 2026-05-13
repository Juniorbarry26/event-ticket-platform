package com.alsaineybarry.tickets.repositories;

import com.alsaineybarry.tickets.domain.entities.User;
import com.alsaineybarry.tickets.domain.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserAttendingEventsRepository extends JpaRepository<User, UUID> {

    @Query("SELECT u FROM User u JOIN u.attendingEvents e WHERE e.id = :eventId")
    List<User> findUsersAttendingEvent(@Param("eventId") UUID eventId);

    @Query("SELECT e FROM Event e JOIN e.attendees u WHERE u.id = :userId")
    List<Event> findEventsAttendedByUser(@Param("userId") UUID userId);

    @Query("SELECT COUNT(u) FROM User u JOIN u.attendingEvents e WHERE e.id = :eventId")
    Long countAttendeesForEvent(@Param("eventId") UUID eventId);

    @Query("SELECT COUNT(e) FROM Event e JOIN e.attendees u WHERE u.id = :userId")
    Long countEventsAttendedByUser(@Param("userId") UUID userId);
}
