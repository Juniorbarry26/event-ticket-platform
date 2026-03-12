package com.alsaineybarry.tickets.services;

import com.alsaineybarry.tickets.domain.entities.Ticket;
import java.util.UUID;

public interface TicketTypeService {
    Ticket purchaseTicket(UUID userId, UUID ticketTypeId);
}
