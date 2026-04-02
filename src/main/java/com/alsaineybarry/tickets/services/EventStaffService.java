package com.alsaineybarry.tickets.services;

import com.alsaineybarry.tickets.domain.dtos.EventStaffDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EventStaffService {
    
    Page<EventStaffDto> getEventStaff(UUID organizerId, UUID eventId, Pageable pageable);
    
    void addStaffToEvent(UUID organizerId, UUID eventId, UUID staffUserId);
    
    void removeStaffFromEvent(UUID organizerId, UUID eventId, UUID staffUserId);
}
