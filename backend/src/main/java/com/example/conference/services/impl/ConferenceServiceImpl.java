package com.example.conference.services.impl;

import com.example.conference.domain.CreateConferenceRequest;
import com.example.conference.domain.CreatePassTierRequest;
import com.example.conference.domain.CreateSessionRequest;
import com.example.conference.domain.UpdateConferenceRequest;
import com.example.conference.domain.UpdatePassTierRequest;
import com.example.conference.domain.UpdateSessionRequest;
import com.example.conference.domain.entities.Conference;
import com.example.conference.domain.entities.ConferenceStatusEnum;
import com.example.conference.domain.entities.PassTier;
import com.example.conference.domain.entities.Session;
import com.example.conference.domain.entities.Speaker;
import com.example.conference.domain.entities.User;
import com.example.conference.exceptions.ConferenceNotFoundException;
import com.example.conference.exceptions.ConferenceUpdateException;
import com.example.conference.exceptions.PassTierNotFoundException;
import com.example.conference.exceptions.SessionNotFoundException;
import com.example.conference.exceptions.SpeakerNotFoundException;
import com.example.conference.exceptions.UserNotFoundException;
import com.example.conference.repositories.ConferenceRepository;
import com.example.conference.repositories.SpeakerRepository;
import com.example.conference.repositories.UserRepository;
import com.example.conference.services.ConferenceService;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConferenceServiceImpl implements ConferenceService {

  private final UserRepository userRepository;
  private final ConferenceRepository conferenceRepository;
  private final SpeakerRepository speakerRepository;

  @Override
  @Transactional
  public Conference createConference(UUID organizerId, CreateConferenceRequest request) {
    User organizer = userRepository.findById(organizerId)
        .orElseThrow(() -> new UserNotFoundException(
            String.format("User with ID %s was not found", organizerId)
        ));

    Conference conference = new Conference();
    conference.setName(request.getName());
    conference.setStart(request.getStart());
    conference.setEnd(request.getEnd());
    conference.setVenue(request.getVenue());
    conference.setSalesStart(request.getSalesStart());
    conference.setSalesEnd(request.getSalesEnd());
    conference.setStatus(request.getStatus());
    conference.setOrganizer(organizer);

    List<PassTier> passTiers = request.getPassTiers().stream().map(pt -> {
      PassTier passTier = new PassTier();
      passTier.setName(pt.getName());
      passTier.setPrice(pt.getPrice());
      passTier.setDescription(pt.getDescription());
      passTier.setTotalAvailable(pt.getTotalAvailable());
      passTier.setConference(conference);
      return passTier;
    }).collect(Collectors.toList());
    conference.setPassTiers(passTiers);

    List<Session> sessions = request.getSessions().stream().map(s -> {
      Session session = new Session();
      session.setTitle(s.getTitle());
      session.setDescription(s.getDescription());
      session.setRoom(s.getRoom());
      session.setStart(s.getStart());
      session.setEnd(s.getEnd());
      session.setConference(conference);
      session.setSpeaker(resolveSpeaker(s.getSpeakerId()));
      return session;
    }).collect(Collectors.toList());
    conference.setSessions(sessions);

    return conferenceRepository.save(conference);
  }

  @Override
  public Page<Conference> listConferencesForOrganizer(UUID organizerId, Pageable pageable) {
    return conferenceRepository.findByOrganizerId(organizerId, pageable);
  }

  @Override
  public Optional<Conference> getConferenceForOrganizer(UUID organizerId, UUID conferenceId) {
    return conferenceRepository.findByIdAndOrganizerId(conferenceId, organizerId);
  }

  @Override
  @Transactional
  public Conference updateConferenceForOrganizer(UUID organizerId, UUID conferenceId,
      UpdateConferenceRequest request) {

    if (null == request.getId()) {
      throw new ConferenceUpdateException("Conference ID cannot be null");
    }

    if (!conferenceId.equals(request.getId())) {
      throw new ConferenceUpdateException(
          "Cannot update the ID of a conference");
    }

    Conference conference = conferenceRepository
        .findByIdAndOrganizerId(conferenceId, organizerId)
        .orElseThrow(() -> new ConferenceNotFoundException(
            String.format("Conference with ID %s was not found", conferenceId)
        ));

    conference.setName(request.getName());
    conference.setStart(request.getStart());
    conference.setEnd(request.getEnd());
    conference.setVenue(request.getVenue());
    conference.setSalesStart(request.getSalesStart());
    conference.setSalesEnd(request.getSalesEnd());
    conference.setStatus(request.getStatus());

    reconcilePassTiers(conference, request.getPassTiers());
    reconcileSessions(conference, request.getSessions());

    return conferenceRepository.save(conference);
  }

  private void reconcilePassTiers(Conference conference,
      List<UpdatePassTierRequest> requestPassTiers) {
    Set<UUID> requestIds = requestPassTiers.stream()
        .map(UpdatePassTierRequest::getId)
        .filter(id -> null != id)
        .collect(Collectors.toSet());

    // Remove pass tiers no longer present in the request
    conference.getPassTiers().removeIf(existing -> !requestIds.contains(existing.getId()));

    for (UpdatePassTierRequest requestPassTier : requestPassTiers) {
      if (null == requestPassTier.getId()) {
        // New pass tier
        PassTier passTier = new PassTier();
        passTier.setName(requestPassTier.getName());
        passTier.setPrice(requestPassTier.getPrice());
        passTier.setDescription(requestPassTier.getDescription());
        passTier.setTotalAvailable(requestPassTier.getTotalAvailable());
        passTier.setConference(conference);
        conference.getPassTiers().add(passTier);
      } else {
        // Update existing pass tier
        PassTier existing = conference.getPassTiers().stream()
            .filter(pt -> pt.getId().equals(requestPassTier.getId()))
            .findFirst()
            .orElseThrow(() -> new PassTierNotFoundException(
                String.format("Pass tier with ID %s was not found", requestPassTier.getId())
            ));
        existing.setName(requestPassTier.getName());
        existing.setPrice(requestPassTier.getPrice());
        existing.setDescription(requestPassTier.getDescription());
        existing.setTotalAvailable(requestPassTier.getTotalAvailable());
      }
    }
  }

  private void reconcileSessions(Conference conference,
      List<UpdateSessionRequest> requestSessions) {
    Set<UUID> requestIds = requestSessions.stream()
        .map(UpdateSessionRequest::getId)
        .filter(id -> null != id)
        .collect(Collectors.toSet());

    // Remove sessions no longer present in the request
    conference.getSessions().removeIf(existing -> !requestIds.contains(existing.getId()));

    for (UpdateSessionRequest requestSession : requestSessions) {
      if (null == requestSession.getId()) {
        // New session
        Session session = new Session();
        session.setTitle(requestSession.getTitle());
        session.setDescription(requestSession.getDescription());
        session.setRoom(requestSession.getRoom());
        session.setStart(requestSession.getStart());
        session.setEnd(requestSession.getEnd());
        session.setConference(conference);
        session.setSpeaker(resolveSpeaker(requestSession.getSpeakerId()));
        conference.getSessions().add(session);
      } else {
        // Update existing session
        Session existing = conference.getSessions().stream()
            .filter(s -> s.getId().equals(requestSession.getId()))
            .findFirst()
            .orElseThrow(() -> new SessionNotFoundException(
                String.format("Session with ID %s was not found", requestSession.getId())
            ));
        existing.setTitle(requestSession.getTitle());
        existing.setDescription(requestSession.getDescription());
        existing.setRoom(requestSession.getRoom());
        existing.setStart(requestSession.getStart());
        existing.setEnd(requestSession.getEnd());
        existing.setSpeaker(resolveSpeaker(requestSession.getSpeakerId()));
      }
    }
  }

  private Speaker resolveSpeaker(UUID speakerId) {
    if (null == speakerId) {
      return null;
    }
    return speakerRepository.findById(speakerId)
        .orElseThrow(() -> new SpeakerNotFoundException(
            String.format("Speaker with ID %s was not found", speakerId)
        ));
  }

  @Override
  @Transactional
  public void deleteConferenceForOrganizer(UUID organizerId, UUID conferenceId) {
    getConferenceForOrganizer(organizerId, conferenceId)
        .ifPresent(conferenceRepository::delete);
  }

  @Override
  public Page<Conference> listPublishedConferences(Pageable pageable) {
    return conferenceRepository.findByStatus(ConferenceStatusEnum.PUBLISHED, pageable);
  }

  @Override
  public Page<Conference> searchPublishedConferences(String query, Pageable pageable) {
    return conferenceRepository.searchConferences(query, pageable);
  }

  @Override
  public Optional<Conference> getPublishedConference(UUID conferenceId) {
    return conferenceRepository.findByIdAndStatus(conferenceId, ConferenceStatusEnum.PUBLISHED);
  }
}
