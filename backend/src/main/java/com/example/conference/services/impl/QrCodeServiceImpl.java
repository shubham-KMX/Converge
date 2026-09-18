package com.example.conference.services.impl;

import com.example.conference.domain.entities.Badge;
import com.example.conference.domain.entities.QrCode;
import com.example.conference.domain.entities.QrCodeStatusEnum;
import com.example.conference.exceptions.QrCodeGenerationException;
import com.example.conference.exceptions.QrCodeNotFoundException;
import com.example.conference.repositories.QrCodeRepository;
import com.example.conference.services.QrCodeService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
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
  public QrCode generateQrCode(Badge badge) {
    try {
      UUID uniqueId = UUID.randomUUID();
      String qrCodeImage = generateQrCodeImage(uniqueId);

      QrCode qrCode = new QrCode();
      qrCode.setId(uniqueId);
      qrCode.setStatus(QrCodeStatusEnum.ACTIVE);
      qrCode.setValue(qrCodeImage);
      qrCode.setBadge(badge);

      return qrCodeRepository.saveAndFlush(qrCode);

    } catch (IOException | WriterException ex) {
      throw new QrCodeGenerationException("Failed to generate QR Code", ex);
    }
  }

  @Override
  public byte[] getQrCodeImageForAttendeeAndBadge(UUID attendeeId, UUID badgeId) {
    QrCode qrCode = qrCodeRepository.findByBadgeIdAndBadgeAttendeeId(badgeId, attendeeId)
        .orElseThrow(QrCodeNotFoundException::new);

    try {
      return Base64.getDecoder().decode(qrCode.getValue());
    } catch (IllegalArgumentException ex) {
      log.error("Invalid base64 QR Code for badge ID: {}", badgeId, ex);
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

    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      ImageIO.write(qrCodeImage, "PNG", baos);
      byte[] imageBytes = baos.toByteArray();

      return Base64.getEncoder().encodeToString(imageBytes);
    }
  }
}
