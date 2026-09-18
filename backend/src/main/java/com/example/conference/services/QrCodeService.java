package com.example.conference.services;

import com.example.conference.domain.entities.Badge;
import com.example.conference.domain.entities.QrCode;
import java.util.UUID;

public interface QrCodeService {

  QrCode generateQrCode(Badge badge);

  byte[] getQrCodeImageForAttendeeAndBadge(UUID attendeeId, UUID badgeId);
}
