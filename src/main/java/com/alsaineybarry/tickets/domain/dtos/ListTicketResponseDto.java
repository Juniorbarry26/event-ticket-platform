package com.alsaineybarry.tickets.domain.dtos;

import java.util.UUID;

import com.alsaineybarry.tickets.domain.enums.TIcketStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListTicketResponseDto {
    private UUID id;
    private TIcketStatusEnum status;
    private ListTicketTicketTypeResponseDto ticketType;
}