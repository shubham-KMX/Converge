package com.example.conference.services.impl;

import com.example.conference.domain.entities.Badge;
import com.example.conference.repositories.BadgeRepository;
import com.example.conference.services.BadgeService;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BadgeServiceImpl implements BadgeService {

  private final BadgeRepository badgeRepository;

  @Override
  public Page<Badge> listBadgesForAttendee(UUID attendeeId, Pageable pageable) {
    return badgeRepository.findByAttendeeId(attendeeId, pageable);
  }

  @Override
  public Optional<Badge> getBadgeForAttendee(UUID attendeeId, UUID badgeId) {
    return badgeRepository.findByIdAndAttendeeId(badgeId, attendeeId);
  }
}
