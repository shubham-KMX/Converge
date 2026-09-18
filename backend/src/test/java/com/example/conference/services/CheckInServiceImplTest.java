package com.example.conference.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.conference.domain.entities.Badge;
import com.example.conference.domain.entities.CheckIn;
import com.example.conference.domain.entities.CheckInMethod;
import com.example.conference.domain.entities.CheckInStatusEnum;
import com.example.conference.domain.entities.QrCode;
import com.example.conference.domain.entities.QrCodeStatusEnum;
import com.example.conference.exceptions.BadgeNotFoundException;
import com.example.conference.exceptions.QrCodeNotFoundException;
import com.example.conference.repositories.BadgeRepository;
import com.example.conference.repositories.CheckInRepository;
import com.example.conference.repositories.QrCodeRepository;
import com.example.conference.services.impl.CheckInServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CheckInServiceImplTest {

  @Mock
  private QrCodeRepository qrCodeRepository;
  @Mock
  private CheckInRepository checkInRepository;
  @Mock
  private BadgeRepository badgeRepository;

  @InjectMocks
  private CheckInServiceImpl checkInService;

  @Test
  void checkInByQrCode_firstScan_isValid() {
    UUID qrId = UUID.randomUUID();
    Badge badge = new Badge();
    badge.setCheckIns(new ArrayList<>());
    QrCode qrCode = new QrCode();
    qrCode.setBadge(badge);

    when(qrCodeRepository.findByIdAndStatus(qrId, QrCodeStatusEnum.ACTIVE))
        .thenReturn(Optional.of(qrCode));
    when(checkInRepository.save(any(CheckIn.class))).thenAnswer(inv -> inv.getArgument(0));

    CheckIn result = checkInService.checkInByQrCode(qrId);

    assertThat(result.getStatus()).isEqualTo(CheckInStatusEnum.VALID);
    assertThat(result.getCheckInMethod()).isEqualTo(CheckInMethod.QR_SCAN);
    assertThat(result.getBadge()).isEqualTo(badge);
  }

  @Test
  void checkInByQrCode_doubleScan_isInvalid() {
    UUID qrId = UUID.randomUUID();
    Badge badge = new Badge();
    CheckIn priorValid = new CheckIn();
    priorValid.setStatus(CheckInStatusEnum.VALID);
    badge.setCheckIns(new ArrayList<>(List.of(priorValid)));

    QrCode qrCode = new QrCode();
    qrCode.setBadge(badge);

    when(qrCodeRepository.findByIdAndStatus(qrId, QrCodeStatusEnum.ACTIVE))
        .thenReturn(Optional.of(qrCode));
    when(checkInRepository.save(any(CheckIn.class))).thenAnswer(inv -> inv.getArgument(0));

    CheckIn result = checkInService.checkInByQrCode(qrId);

    assertThat(result.getStatus()).isEqualTo(CheckInStatusEnum.INVALID);
  }

  @Test
  void checkInByQrCode_throws_whenQrNotFound() {
    UUID qrId = UUID.randomUUID();
    when(qrCodeRepository.findByIdAndStatus(qrId, QrCodeStatusEnum.ACTIVE))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() -> checkInService.checkInByQrCode(qrId))
        .isInstanceOf(QrCodeNotFoundException.class);
  }

  @Test
  void checkInManually_firstScan_isValidWithManualMethod() {
    UUID badgeId = UUID.randomUUID();
    Badge badge = new Badge();
    badge.setCheckIns(new ArrayList<>());

    when(badgeRepository.findById(badgeId)).thenReturn(Optional.of(badge));
    when(checkInRepository.save(any(CheckIn.class))).thenAnswer(inv -> inv.getArgument(0));

    CheckIn result = checkInService.checkInManually(badgeId);

    assertThat(result.getStatus()).isEqualTo(CheckInStatusEnum.VALID);
    assertThat(result.getCheckInMethod()).isEqualTo(CheckInMethod.MANUAL);
  }

  @Test
  void checkInManually_throws_whenBadgeNotFound() {
    UUID badgeId = UUID.randomUUID();
    when(badgeRepository.findById(badgeId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> checkInService.checkInManually(badgeId))
        .isInstanceOf(BadgeNotFoundException.class);
  }
}
