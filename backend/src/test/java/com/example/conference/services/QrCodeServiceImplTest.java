package com.example.conference.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.conference.domain.entities.Badge;
import com.example.conference.domain.entities.QrCode;
import com.example.conference.domain.entities.QrCodeStatusEnum;
import com.example.conference.exceptions.QrCodeNotFoundException;
import com.example.conference.repositories.QrCodeRepository;
import com.example.conference.services.impl.QrCodeServiceImpl;
import com.google.zxing.qrcode.QRCodeWriter;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QrCodeServiceImplTest {

  @Spy
  private QRCodeWriter qrCodeWriter = new QRCodeWriter();
  @Mock
  private QrCodeRepository qrCodeRepository;

  @InjectMocks
  private QrCodeServiceImpl qrCodeService;

  @Test
  void generateQrCode_producesActiveQrWithBase64PngValue() {
    Badge badge = new Badge();
    when(qrCodeRepository.saveAndFlush(any(QrCode.class))).thenAnswer(inv -> inv.getArgument(0));

    QrCode qrCode = qrCodeService.generateQrCode(badge);

    assertThat(qrCode.getId()).isNotNull();
    assertThat(qrCode.getStatus()).isEqualTo(QrCodeStatusEnum.ACTIVE);
    assertThat(qrCode.getBadge()).isEqualTo(badge);
    assertThat(qrCode.getValue()).isNotBlank();

    // Value must be a base64-encoded PNG (signature bytes 137 80 78 71)
    byte[] decoded = Base64.getDecoder().decode(qrCode.getValue());
    assertThat(decoded[0] & 0xFF).isEqualTo(137);
    assertThat(decoded[1] & 0xFF).isEqualTo(80);
    assertThat(decoded[2] & 0xFF).isEqualTo(78);
    assertThat(decoded[3] & 0xFF).isEqualTo(71);
  }

  @Test
  void getQrCodeImageForAttendeeAndBadge_decodesStoredBase64() {
    UUID attendeeId = UUID.randomUUID();
    UUID badgeId = UUID.randomUUID();
    String payload = Base64.getEncoder().encodeToString(new byte[]{1, 2, 3, 4});

    QrCode qrCode = new QrCode();
    qrCode.setValue(payload);
    when(qrCodeRepository.findByBadgeIdAndBadgeAttendeeId(badgeId, attendeeId))
        .thenReturn(Optional.of(qrCode));

    byte[] result = qrCodeService.getQrCodeImageForAttendeeAndBadge(attendeeId, badgeId);

    assertThat(result).containsExactly(1, 2, 3, 4);
  }

  @Test
  void getQrCodeImageForAttendeeAndBadge_throws_whenNotFound() {
    UUID attendeeId = UUID.randomUUID();
    UUID badgeId = UUID.randomUUID();
    when(qrCodeRepository.findByBadgeIdAndBadgeAttendeeId(badgeId, attendeeId))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() -> qrCodeService.getQrCodeImageForAttendeeAndBadge(attendeeId, badgeId))
        .isInstanceOf(QrCodeNotFoundException.class);
  }
}
