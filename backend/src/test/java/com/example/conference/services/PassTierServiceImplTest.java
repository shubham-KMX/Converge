package com.example.conference.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.example.conference.services.impl.PassTierServiceImpl;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PassTierServiceImplTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private PassTierRepository passTierRepository;
  @Mock
  private BadgeRepository badgeRepository;
  @Mock
  private QrCodeService qrCodeService;

  @InjectMocks
  private PassTierServiceImpl passTierService;

  @Test
  void purchasePass_createsBadgeAndGeneratesQrCode_whenAvailable() {
    UUID userId = UUID.randomUUID();
    UUID passTierId = UUID.randomUUID();

    User user = new User();
    user.setId(userId);

    PassTier passTier = new PassTier();
    passTier.setId(passTierId);
    passTier.setTotalAvailable(100);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(passTierRepository.findByIdWithLock(passTierId)).thenReturn(Optional.of(passTier));
    when(badgeRepository.countByPassTierId(passTierId)).thenReturn(10);
    when(badgeRepository.save(any(Badge.class))).thenAnswer(inv -> inv.getArgument(0));

    passTierService.purchasePass(userId, passTierId);

    ArgumentCaptor<Badge> captor = ArgumentCaptor.forClass(Badge.class);
    verify(badgeRepository, org.mockito.Mockito.atLeastOnce()).save(captor.capture());
    Badge saved = captor.getValue();

    assertThat(saved.getStatus()).isEqualTo(BadgeStatusEnum.PURCHASED);
    assertThat(saved.getPassTier()).isEqualTo(passTier);
    assertThat(saved.getAttendee()).isEqualTo(user);
    verify(qrCodeService).generateQrCode(any(Badge.class));
  }

  @Test
  void purchasePass_throwsSoldOut_whenNoneRemaining() {
    UUID userId = UUID.randomUUID();
    UUID passTierId = UUID.randomUUID();

    User user = new User();
    user.setId(userId);

    PassTier passTier = new PassTier();
    passTier.setId(passTierId);
    passTier.setTotalAvailable(50);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(passTierRepository.findByIdWithLock(passTierId)).thenReturn(Optional.of(passTier));
    when(badgeRepository.countByPassTierId(passTierId)).thenReturn(50);

    assertThatThrownBy(() -> passTierService.purchasePass(userId, passTierId))
        .isInstanceOf(PassesSoldOutException.class);

    verify(badgeRepository, never()).save(any(Badge.class));
    verify(qrCodeService, never()).generateQrCode(any(Badge.class));
  }

  @Test
  void purchasePass_throwsUserNotFound_whenUserMissing() {
    UUID userId = UUID.randomUUID();
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> passTierService.purchasePass(userId, UUID.randomUUID()))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void purchasePass_throwsPassTierNotFound_whenTierMissing() {
    UUID userId = UUID.randomUUID();
    UUID passTierId = UUID.randomUUID();
    when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
    when(passTierRepository.findByIdWithLock(passTierId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> passTierService.purchasePass(userId, passTierId))
        .isInstanceOf(PassTierNotFoundException.class);
  }
}
