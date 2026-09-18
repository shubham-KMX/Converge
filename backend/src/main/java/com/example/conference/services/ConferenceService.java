package com.example.conference.services;

import com.example.conference.domain.CreateConferenceRequest;
import com.example.conference.domain.UpdateConferenceRequest;
import com.example.conference.domain.entities.Conference;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConferenceService {

  Conference createConference(UUID organizerId, CreateConferenceRequest request);

  Page<Conference> listConferencesForOrganizer(UUID organizerId, Pageable pageable);

  Optional<Conference> getConferenceForOrganizer(UUID organizerId, UUID conferenceId);

  Conference updateConferenceForOrganizer(UUID organizerId, UUID conferenceId,
      UpdateConferenceRequest request);

  void deleteConferenceForOrganizer(UUID organizerId, UUID conferenceId);

  Page<Conference> listPublishedConferences(Pageable pageable);

  Page<Conference> searchPublishedConferences(String query, Pageable pageable);

  Optional<Conference> getPublishedConference(UUID conferenceId);
}
