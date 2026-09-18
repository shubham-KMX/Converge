package com.example.conference.services;

import com.example.conference.domain.entities.CheckIn;
import java.util.UUID;

public interface CheckInService {

  CheckIn checkInByQrCode(UUID qrCodeId);

  CheckIn checkInManually(UUID badgeId);
}
