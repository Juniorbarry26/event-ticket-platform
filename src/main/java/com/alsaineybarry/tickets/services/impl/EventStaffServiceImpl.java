package com.alsaineybarry.tickets.services.impl;

import com.alsaineybarry.tickets.domain.dtos.EventStaffDto;
import com.alsaineybarry.tickets.domain.entities.Event;
import com.alsaineybarry.tickets.domain.entities.User;
import com.alsaineybarry.tickets.exceptions.EventNotFoundException;
import com.alsaineybarry.tickets.exceptions.UserNotFoundException;
import com.alsaineybarry.tickets.mappers.EventStaffMapper;
import com.alsaineybarry.tickets.repositories.EventRepository;
import com.alsaineybarry.tickets.repositories.UserRepository;
import com.alsaineybarry.tickets.services.EventStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EventStaffServiceImpl implements EventStaffService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventStaffMapper eventStaffMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<EventStaffDto> getEventStaff(UUID organizerId, UUID eventId, Pageable pageable) {
        Event event = eventRepository.findByIdAndOrganizerId(eventId, organizerId)
                .orElseThrow(EventNotFoundException::new);
        
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), event.getStaffs().size());
        
        List<EventStaffDto> staffList = event.getStaffs().subList(start, end).stream()
                .map(eventStaffMapper::toEventStaffDto)
                .collect(java.util.stream.Collectors.toList());
        
        return new PageImpl<>(staffList, pageable, event.getStaffs().size());
    }

    @Override
    public void addStaffToEvent(UUID organizerId, UUID eventId, UUID staffUserId) {
        Event event = eventRepository.findByIdAndOrganizerId(eventId, organizerId)
                .orElseThrow(EventNotFoundException::new);
        
        User staffUser = userRepository.findById(staffUserId)
                .orElseThrow(UserNotFoundException::new);
        
        if (!event.getStaffs().contains(staffUser)) {
            event.getStaffs().add(staffUser);
            staffUser.getStaffingEvents().add(event);
            eventRepository.save(event);
        }
    }

    @Override
    public void removeStaffFromEvent(UUID organizerId, UUID eventId, UUID staffUserId) {
        Event event = eventRepository.findByIdAndOrganizerId(eventId, organizerId)
                .orElseThrow(EventNotFoundException::new);
        
        User staffUser = userRepository.findById(staffUserId)
                .orElseThrow(UserNotFoundException::new);
        
        event.getStaffs().remove(staffUser);
        staffUser.getStaffingEvents().remove(event);
        eventRepository.save(event);
    }
}
