package com.example.conference.controllers;

import com.example.conference.domain.dtos.CreateSpeakerRequestDto;
import com.example.conference.domain.dtos.SpeakerResponseDto;
import com.example.conference.domain.entities.Speaker;
import com.example.conference.mappers.SpeakerMapper;
import com.example.conference.services.SpeakerService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/speakers")
@RequiredArgsConstructor
public class SpeakerController {

  private final SpeakerService speakerService;
  private final SpeakerMapper speakerMapper;

  @PostMapping
  public ResponseEntity<SpeakerResponseDto> createSpeaker(
      @Valid @RequestBody CreateSpeakerRequestDto requestDto) {
    Speaker speaker = speakerService.createSpeaker(
        requestDto.getName(),
        requestDto.getTitle(),
        requestDto.getCompany(),
        requestDto.getBio());
    return new ResponseEntity<>(speakerMapper.toSpeakerResponseDto(speaker), HttpStatus.CREATED);
  }

  @GetMapping
  public List<SpeakerResponseDto> listSpeakers() {
    return speakerService.listSpeakers().stream()
        .map(speakerMapper::toSpeakerResponseDto)
        .toList();
  }

  @GetMapping(path = "/{speakerId}")
  public ResponseEntity<SpeakerResponseDto> getSpeaker(@PathVariable UUID speakerId) {
    Speaker speaker = speakerService.getSpeaker(speakerId);
    return ResponseEntity.ok(speakerMapper.toSpeakerResponseDto(speaker));
  }
}
