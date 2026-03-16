package com.alsaineybarry.tickets.services.impl;

import com.alsaineybarry.tickets.domain.entities.QrCode;
import com.alsaineybarry.tickets.domain.entities.Ticket;
import com.alsaineybarry.tickets.domain.entities.TicketValidation;
import com.alsaineybarry.tickets.domain.enums.TicketValidationMethodEnum;
import com.alsaineybarry.tickets.domain.enums.QrCodeStatusEnum;
import com.alsaineybarry.tickets.domain.enums.TicketValidationStatusEnum;
import com.alsaineybarry.tickets.exceptions.QrCodeNotFoundException;
import com.alsaineybarry.tickets.exceptions.TicketNotFoundException;
import com.alsaineybarry.tickets.repositories.QrCodeRepository;
import com.alsaineybarry.tickets.repositories.TicketRepository;
import com.alsaineybarry.tickets.repositories.TicketValidationRepository;
import com.alsaineybarry.tickets.services.TicketValidationService;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketValidationServiceImpl implements TicketValidationService {

    private final QrCodeRepository qrCodeRepository;
    private final TicketValidationRepository ticketValidationRepository;
    private final TicketRepository ticketRepository;

    @Override
    public TicketValidation validateTicketByQrCode(UUID qrCodeId) {
        QrCode qrCode = qrCodeRepository.findByIdAndStatus(qrCodeId, QrCodeStatusEnum.ACTIVE)
                .orElseThrow(() -> new QrCodeNotFoundException(
                        String.format(
                                "QR Code with ID %s was not found", qrCodeId
                        )
                ));

        Ticket ticket = qrCode.getTicket();

        return validateTicket(ticket, TicketValidationMethodEnum.QR_SCAN);
    }

    private TicketValidation validateTicket(Ticket ticket,
                                            TicketValidationMethodEnum ticketValidationMethod) {
        TicketValidation ticketValidation = new TicketValidation();
        ticketValidation.setTicket(ticket);
        ticketValidation.setValidationMethod(ticketValidationMethod);

        TicketValidationStatusEnum ticketValidationStatus = ticket.getValidations().stream()
                .filter(v -> TicketValidationStatusEnum.VALID.equals(v.getValidationStatus()))
                .findFirst()
                .map(v -> TicketValidationStatusEnum.INVALID)
                .orElse(TicketValidationStatusEnum.VALID);

        ticketValidation.setValidationStatus(ticketValidationStatus);

        return ticketValidationRepository.save(ticketValidation);
    }

    @Override
    public TicketValidation validateTicketManually(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(TicketNotFoundException::new);
        return validateTicket(ticket, TicketValidationMethodEnum.MANUAL);
    }
}