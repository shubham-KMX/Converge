package com.example.conference.services.impl;

import com.example.conference.domain.entities.Session;
import com.example.conference.repositories.SessionRepository;
import com.example.conference.services.SessionService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

  private final SessionRepository sessionRepository;

  @Override
  public List<Session> listSessionsForConference(UUID conferenceId) {
    return sessionRepository.findByConferenceId(conferenceId);
  }

  @Override
  public Optional<Session> getSession(UUID conferenceId, UUID sessionId) {
    return sessionRepository.findByIdAndConferenceId(sessionId, conferenceId);
  }
}
