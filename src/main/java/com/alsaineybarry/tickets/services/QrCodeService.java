package com.alsaineybarry.tickets.services;

import com.alsaineybarry.tickets.domain.entities.QrCode;
import com.alsaineybarry.tickets.domain.entities.Ticket;

import java.util.UUID;

public interface QrCodeService {

    QrCode generateQrCode(Ticket ticket);

    byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId);
}