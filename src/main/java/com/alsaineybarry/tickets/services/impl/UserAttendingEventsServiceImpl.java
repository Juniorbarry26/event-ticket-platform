package com.alsaineybarry.tickets.services.impl;

import com.alsaineybarry.tickets.domain.entities.User;
import com.alsaineybarry.tickets.domain.entities.Event;
import com.alsaineybarry.tickets.repositories.UserAttendingEventsRepository;
import com.alsaineybarry.tickets.services.UserAttendingEventsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAttendingEventsServiceImpl implements UserAttendingEventsService {

    private final UserAttendingEventsRepository userAttendingEventsRepository;

    @Override
    public List<User> getUsersAttendingEvent(UUID eventId) {
        log.debug("Finding users attending event: {}", eventId);
        List<User> users = userAttendingEventsRepository.findUsersAttendingEvent(eventId);
        log.debug("Found {} users attending event: {}", users.size(), eventId);
        return users;
    }

    @Override
    public List<Event> getEventsAttendedByUser(UUID userId) {
        log.debug("Finding events attended by user: {}", userId);
        List<Event> events = userAttendingEventsRepository.findEventsAttendedByUser(userId);
        log.debug("Found {} events attended by user: {}", events.size(), userId);
        return events;
    }

    @Override
    public Long getAttendeeCountForEvent(UUID eventId) {
        log.debug("Counting attendees for event: {}", eventId);
        Long count = userAttendingEventsRepository.countAttendeesForEvent(eventId);
        log.debug("Event {} has {} attendees", eventId, count);
        return count;
    }

    @Override
    public Long getEventCountAttendedByUser(UUID userId) {
        log.debug("Counting events attended by user: {}", userId);
        Long count = userAttendingEventsRepository.countEventsAttendedByUser(userId);
        log.debug("User {} has attended {} events", userId, count);
        return count;
    }
}
