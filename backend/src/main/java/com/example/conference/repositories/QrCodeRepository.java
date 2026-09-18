package com.example.conference.repositories;

import com.example.conference.domain.entities.QrCode;
import com.example.conference.domain.entities.QrCodeStatusEnum;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, UUID> {

  Optional<QrCode> findByBadgeIdAndBadgeAttendeeId(UUID badgeId, UUID attendeeId);

  Optional<QrCode> findByIdAndStatus(UUID id, QrCodeStatusEnum status);
}
