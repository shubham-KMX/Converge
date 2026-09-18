package com.example.conference.services;

import com.example.conference.domain.entities.Session;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionService {

  List<Session> listSessionsForConference(UUID conferenceId);

  Optional<Session> getSession(UUID conferenceId, UUID sessionId);
}
