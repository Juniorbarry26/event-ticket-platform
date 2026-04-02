package com.alsaineybarry.tickets.mappers;

import com.alsaineybarry.tickets.domain.dtos.TicketValidationResponseDto;
import com.alsaineybarry.tickets.domain.entities.TicketValidation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketValidationMapper {

    @Mapping(target = "ticketId", source = "ticket.id")
    @Mapping(target = "userEmail", source = "ticket.purchaser.email")
    @Mapping(target = "userName", source = "ticket.purchaser.name")
    @Mapping(target = "validatedAt", source = "createdAt")
    TicketValidationResponseDto toTicketValidationResponseDto(TicketValidation ticketValidation);

}