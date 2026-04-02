package com.alsaineybarry.tickets.mappers;

import com.alsaineybarry.tickets.domain.dtos.EventStaffDto;
import com.alsaineybarry.tickets.domain.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventStaffMapper {
    
    EventStaffDto toEventStaffDto(User user);
}
