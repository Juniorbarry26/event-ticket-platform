package com.alsaineybarry.tickets.services;

import com.alsaineybarry.tickets.domain.entities.User;
import com.alsaineybarry.tickets.domain.entities.Event;

import java.util.List;
import java.util.UUID;

public interface UserAttendingEventsService {
    List<User> getUsersAttendingEvent(UUID eventId);
    List<Event> getEventsAttendedByUser(UUID userId);
    Long getAttendeeCountForEvent(UUID eventId);
    Long getEventCountAttendedByUser(UUID userId);
}
