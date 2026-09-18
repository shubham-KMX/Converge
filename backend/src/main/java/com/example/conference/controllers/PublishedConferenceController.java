package com.example.conference.controllers;

import com.example.conference.domain.dtos.GetPublishedConferenceDetailsResponseDto;
import com.example.conference.domain.dtos.ListPublishedConferenceResponseDto;
import com.example.conference.mappers.ConferenceMapper;
import com.example.conference.services.ConferenceService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/published-conferences")
@RequiredArgsConstructor
public class PublishedConferenceController {

  private final ConferenceService conferenceService;
  private final ConferenceMapper conferenceMapper;

  @GetMapping
  public Page<ListPublishedConferenceResponseDto> listPublishedConferences(
      @RequestParam(required = false) String q, Pageable pageable) {

    Page<com.example.conference.domain.entities.Conference> conferences;
    if (StringUtils.hasText(q)) {
      conferences = conferenceService.searchPublishedConferences(q, pageable);
    } else {
      conferences = conferenceService.listPublishedConferences(pageable);
    }

    return conferences.map(conferenceMapper::toListPublishedConferenceResponseDto);
  }

  @GetMapping(path = "/{conferenceId}")
  public ResponseEntity<GetPublishedConferenceDetailsResponseDto> getPublishedConference(
      @PathVariable UUID conferenceId) {
    return conferenceService.getPublishedConference(conferenceId)
        .map(conferenceMapper::toGetPublishedConferenceDetailsResponseDto)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
