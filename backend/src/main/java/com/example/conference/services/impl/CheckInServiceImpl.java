package com.example.conference.services.impl;

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
import com.example.conference.services.CheckInService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CheckInServiceImpl implements CheckInService {

  private final QrCodeRepository qrCodeRepository;
  private final CheckInRepository checkInRepository;
  private final BadgeRepository badgeRepository;

  @Override
  public CheckIn checkInByQrCode(UUID qrCodeId) {
    QrCode qrCode = qrCodeRepository.findByIdAndStatus(qrCodeId, QrCodeStatusEnum.ACTIVE)
        .orElseThrow(() -> new QrCodeNotFoundException(
            String.format("QR Code with ID %s was not found", qrCodeId)
        ));

    Badge badge = qrCode.getBadge();

    return checkIn(badge, CheckInMethod.QR_SCAN);
  }

  @Override
  public CheckIn checkInManually(UUID badgeId) {
    Badge badge = badgeRepository.findById(badgeId)
        .orElseThrow(BadgeNotFoundException::new);
    return checkIn(badge, CheckInMethod.MANUAL);
  }

  private CheckIn checkIn(Badge badge, CheckInMethod method) {
    CheckIn checkIn = new CheckIn();
    checkIn.setBadge(badge);
    checkIn.setCheckInMethod(method);

    CheckInStatusEnum status = badge.getCheckIns().stream()
        .filter(c -> CheckInStatusEnum.VALID.equals(c.getStatus()))
        .findFirst()
        .map(c -> CheckInStatusEnum.INVALID)
        .orElse(CheckInStatusEnum.VALID);

    checkIn.setStatus(status);

    return checkInRepository.save(checkIn);
  }
}
