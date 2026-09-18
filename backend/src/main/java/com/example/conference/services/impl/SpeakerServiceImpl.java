package com.example.conference.services.impl;

import com.example.conference.domain.entities.Speaker;
import com.example.conference.exceptions.SpeakerNotFoundException;
import com.example.conference.repositories.SpeakerRepository;
import com.example.conference.services.SpeakerService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SpeakerServiceImpl implements SpeakerService {

  private final SpeakerRepository speakerRepository;

  @Override
  @Transactional
  public Speaker createSpeaker(String name, String title, String company, String bio) {
    Speaker speaker = new Speaker();
    speaker.setName(name);
    speaker.setTitle(title);
    speaker.setCompany(company);
    speaker.setBio(bio);
    return speakerRepository.save(speaker);
  }

  @Override
  public List<Speaker> listSpeakers() {
    return speakerRepository.findAll();
  }

  @Override
  public Speaker getSpeaker(UUID speakerId) {
    return speakerRepository.findById(speakerId)
        .orElseThrow(() -> new SpeakerNotFoundException(
            String.format("Speaker with ID %s was not found", speakerId)
        ));
  }
}
