package com.example.conference.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.conference.domain.CreateConferenceRequest;
import com.example.conference.domain.CreatePassTierRequest;
import com.example.conference.domain.CreateSessionRequest;
import com.example.conference.domain.entities.Conference;
import com.example.conference.domain.entities.ConferenceStatusEnum;
import com.example.conference.domain.entities.Speaker;
import com.example.conference.domain.entities.User;
import com.example.conference.exceptions.SpeakerNotFoundException;
import com.example.conference.exceptions.UserNotFoundException;
import com.example.conference.repositories.ConferenceRepository;
import com.example.conference.repositories.SpeakerRepository;
import com.example.conference.repositories.UserRepository;
import com.example.conference.services.impl.ConferenceServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConferenceServiceImplTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private ConferenceRepository conferenceRepository;
  @Mock
  private SpeakerRepository speakerRepository;

  @InjectMocks
  private ConferenceServiceImpl conferenceService;

  @Test
  void createConference_wiresPassTiersAndSessionsAndResolvesSpeaker() {
    UUID organizerId = UUID.randomUUID();
    UUID speakerId = UUID.randomUUID();

    User organizer = new User();
    organizer.setId(organizerId);
    Speaker speaker = new Speaker();
    speaker.setId(speakerId);
    speaker.setName("Ada");

    when(userRepository.findById(organizerId)).thenReturn(Optional.of(organizer));
    when(speakerRepository.findById(speakerId)).thenReturn(Optional.of(speaker));
    when(conferenceRepository.save(any(Conference.class))).thenAnswer(inv -> inv.getArgument(0));

    CreateConferenceRequest request = CreateConferenceRequest.builder()
        .name("DevConf")
        .venue("Hall")
        .status(ConferenceStatusEnum.PUBLISHED)
        .passTiers(List.of(CreatePassTierRequest.builder()
            .name("GA").price(99.0).totalAvailable(100).build()))
        .sessions(List.of(CreateSessionRequest.builder()
            .title("Keynote").speakerId(speakerId).build()))
        .build();

    Conference result = conferenceService.createConference(organizerId, request);

    assertThat(result.getOrganizer()).isEqualTo(organizer);
    assertThat(result.getPassTiers()).hasSize(1);
    assertThat(result.getPassTiers().get(0).getConference()).isEqualTo(result);
    assertThat(result.getSessions()).hasSize(1);
    assertThat(result.getSessions().get(0).getSpeaker()).isEqualTo(speaker);
    assertThat(result.getSessions().get(0).getConference()).isEqualTo(result);
  }

  @Test
  void createConference_allowsSessionWithNoSpeaker() {
    UUID organizerId = UUID.randomUUID();
    User organizer = new User();
    organizer.setId(organizerId);

    when(userRepository.findById(organizerId)).thenReturn(Optional.of(organizer));
    when(conferenceRepository.save(any(Conference.class))).thenAnswer(inv -> inv.getArgument(0));

    CreateConferenceRequest request = CreateConferenceRequest.builder()
        .name("DevConf")
        .venue("Hall")
        .status(ConferenceStatusEnum.DRAFT)
        .passTiers(List.of())
        .sessions(List.of(CreateSessionRequest.builder().title("Open mic").build()))
        .build();

    Conference result = conferenceService.createConference(organizerId, request);

    assertThat(result.getSessions()).hasSize(1);
    assertThat(result.getSessions().get(0).getSpeaker()).isNull();
  }

  @Test
  void createConference_throwsSpeakerNotFound_whenSpeakerMissing() {
    UUID organizerId = UUID.randomUUID();
    UUID speakerId = UUID.randomUUID();
    User organizer = new User();
    organizer.setId(organizerId);

    when(userRepository.findById(organizerId)).thenReturn(Optional.of(organizer));
    when(speakerRepository.findById(speakerId)).thenReturn(Optional.empty());

    CreateConferenceRequest request = CreateConferenceRequest.builder()
        .name("DevConf")
        .venue("Hall")
        .status(ConferenceStatusEnum.DRAFT)
        .passTiers(List.of())
        .sessions(List.of(CreateSessionRequest.builder()
            .title("Ghost").speakerId(speakerId).build()))
        .build();

    assertThatThrownBy(() -> conferenceService.createConference(organizerId, request))
        .isInstanceOf(SpeakerNotFoundException.class);
  }

  @Test
  void createConference_throwsUserNotFound_whenOrganizerMissing() {
    UUID organizerId = UUID.randomUUID();
    when(userRepository.findById(organizerId)).thenReturn(Optional.empty());

    CreateConferenceRequest request = CreateConferenceRequest.builder()
        .name("DevConf").venue("Hall").status(ConferenceStatusEnum.DRAFT)
        .passTiers(List.of()).sessions(List.of()).build();

    assertThatThrownBy(() -> conferenceService.createConference(organizerId, request))
        .isInstanceOf(UserNotFoundException.class);
  }
}
