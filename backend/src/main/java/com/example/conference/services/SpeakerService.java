package com.example.conference.services;

import com.example.conference.domain.entities.Speaker;
import java.util.List;
import java.util.UUID;

public interface SpeakerService {

  Speaker createSpeaker(String name, String title, String company, String bio);

  List<Speaker> listSpeakers();

  Speaker getSpeaker(UUID speakerId);
}
