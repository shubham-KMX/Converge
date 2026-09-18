package com.example.conference.services;

import com.example.conference.domain.entities.Badge;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BadgeService {

  Page<Badge> listBadgesForAttendee(UUID attendeeId, Pageable pageable);

  Optional<Badge> getBadgeForAttendee(UUID attendeeId, UUID badgeId);
}
