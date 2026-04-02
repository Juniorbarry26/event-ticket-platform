package com.alsaineybarry.tickets.services.impl;

import com.alsaineybarry.tickets.domain.entities.QrCode;
import com.alsaineybarry.tickets.domain.entities.Ticket;
import com.alsaineybarry.tickets.domain.enums.QrCodeStatusEnum;
import com.alsaineybarry.tickets.exceptions.QrCodeGenerationException;
import com.alsaineybarry.tickets.exceptions.QrCodeNotFoundException;
import com.alsaineybarry.tickets.repositories.QrCodeRepository;
import com.alsaineybarry.tickets.services.QrCodeService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class QrCodeServiceImpl implements QrCodeService {

    private static final int QR_HEIGHT = 300;
    private static final int QR_WIDTH = 300;

    private final QRCodeWriter qrCodeWriter;
    private final QrCodeRepository qrCodeRepository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public QrCode generateQrCode(Ticket ticket) {
        try {
            UUID uniqueId = UUID.randomUUID();
            // Store just the UUID, generate image on demand
            String qrCodeId = uniqueId.toString();

            QrCode qrCode = new QrCode();
            qrCode.setStatus(QrCodeStatusEnum.ACTIVE);
            qrCode.setCodeValue(qrCodeId.getBytes());
            qrCode.setTicket(ticket);

            return qrCodeRepository.saveAndFlush(qrCode);

        } catch(Exception ex) {
            throw new QrCodeGenerationException("Failed to generate QR Code", ex);
        }
    }

    @Override
    public byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId) {
        QrCode qrCode = qrCodeRepository.findByTicketIdAndTicketPurchaserId(ticketId, userId)
                .orElseThrow(QrCodeNotFoundException::new);

        try {
            String qrCodeId = new String(qrCode.getCodeValue());
            String base64Image = generateQrCodeImage(UUID.fromString(qrCodeId));
            return Base64.getDecoder().decode(base64Image);
        } catch(Exception ex) {
            log.error("Invalid base64 QR Code for ticket ID: {}", ticketId, ex);
            throw new QrCodeNotFoundException();
        }
    }

    private String generateQrCodeImage(UUID uniqueId) throws WriterException, IOException {
        BitMatrix bitMatrix = qrCodeWriter.encode(
                uniqueId.toString(),
                BarcodeFormat.QR_CODE,
                QR_WIDTH,
                QR_HEIGHT
        );

        BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(qrCodeImage, "PNG", baos);
            byte[] imageBytes = baos.toByteArray();

            return Base64.getEncoder().encodeToString(imageBytes);
        }

    }

}