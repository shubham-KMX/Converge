package com.example.conference.repositories;

import com.example.conference.domain.entities.Badge;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, UUID> {

  int countByPassTierId(UUID passTierId);

  Page<Badge> findByAttendeeId(UUID attendeeId, Pageable pageable);

  Optional<Badge> findByIdAndAttendeeId(UUID id, UUID attendeeId);
}
