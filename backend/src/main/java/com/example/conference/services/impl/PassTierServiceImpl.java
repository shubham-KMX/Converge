package com.example.conference.services.impl;

import com.example.conference.domain.entities.Badge;
import com.example.conference.domain.entities.BadgeStatusEnum;
import com.example.conference.domain.entities.PassTier;
import com.example.conference.domain.entities.User;
import com.example.conference.exceptions.PassTierNotFoundException;
import com.example.conference.exceptions.PassesSoldOutException;
import com.example.conference.exceptions.UserNotFoundException;
import com.example.conference.repositories.BadgeRepository;
import com.example.conference.repositories.PassTierRepository;
import com.example.conference.repositories.UserRepository;
import com.example.conference.services.PassTierService;
import com.example.conference.services.QrCodeService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PassTierServiceImpl implements PassTierService {

  private final UserRepository userRepository;
  private final PassTierRepository passTierRepository;
  private final BadgeRepository badgeRepository;
  private final QrCodeService qrCodeService;

  @Override
  @Transactional
  public Badge purchasePass(UUID attendeeId, UUID passTierId) {
    User user = userRepository.findById(attendeeId).orElseThrow(() -> new UserNotFoundException(
        String.format("User with ID %s was not found", attendeeId)
    ));

    PassTier passTier = passTierRepository.findByIdWithLock(passTierId)
        .orElseThrow(() -> new PassTierNotFoundException(
            String.format("Pass tier with ID %s was not found", passTierId)
        ));

    int purchasedPasses = badgeRepository.countByPassTierId(passTier.getId());
    Integer totalAvailable = passTier.getTotalAvailable();

    if (purchasedPasses + 1 > totalAvailable) {
      throw new PassesSoldOutException();
    }

    Badge badge = new Badge();
    badge.setStatus(BadgeStatusEnum.PURCHASED);
    badge.setPassTier(passTier);
    badge.setAttendee(user);

    Badge savedBadge = badgeRepository.save(badge);
    qrCodeService.generateQrCode(savedBadge);

    return badgeRepository.save(savedBadge);
  }
}
