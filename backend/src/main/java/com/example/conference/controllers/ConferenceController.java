package com.example.conference.controllers;

import com.example.conference.domain.CreateConferenceRequest;
import com.example.conference.domain.UpdateConferenceRequest;
import com.example.conference.domain.dtos.CreateConferenceRequestDto;
import com.example.conference.domain.dtos.CreateConferenceResponseDto;
import com.example.conference.domain.dtos.GetConferenceDetailsResponseDto;
import com.example.conference.domain.dtos.ListConferenceResponseDto;
import com.example.conference.domain.dtos.UpdateConferenceRequestDto;
import com.example.conference.domain.dtos.UpdateConferenceResponseDto;
import com.example.conference.domain.entities.Conference;
import com.example.conference.mappers.ConferenceMapper;
import com.example.conference.services.ConferenceService;
import com.example.conference.util.JwtUtil;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/conferences")
@RequiredArgsConstructor
public class ConferenceController {

  private final ConferenceService conferenceService;
  private final ConferenceMapper conferenceMapper;

  @PostMapping
  public ResponseEntity<CreateConferenceResponseDto> createConference(
      @AuthenticationPrincipal Jwt jwt,
      @Valid @RequestBody CreateConferenceRequestDto requestDto) {

    CreateConferenceRequest request = conferenceMapper.fromDto(requestDto);
    UUID organizerId = JwtUtil.parseUserId(jwt);
    Conference conference = conferenceService.createConference(organizerId, request);
    CreateConferenceResponseDto responseDto =
        conferenceMapper.toCreateConferenceResponseDto(conference);

    return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
  }

  @GetMapping
  public Page<ListConferenceResponseDto> listConferences(
      @AuthenticationPrincipal Jwt jwt, Pageable pageable) {
    UUID organizerId = JwtUtil.parseUserId(jwt);
    return conferenceService.listConferencesForOrganizer(organizerId, pageable)
        .map(conferenceMapper::toListConferenceResponseDto);
  }

  @GetMapping(path = "/{conferenceId}")
  public ResponseEntity<GetConferenceDetailsResponseDto> getConference(
      @AuthenticationPrincipal Jwt jwt, @PathVariable UUID conferenceId) {
    UUID organizerId = JwtUtil.parseUserId(jwt);
    return conferenceService.getConferenceForOrganizer(organizerId, conferenceId)
        .map(conferenceMapper::toGetConferenceDetailsResponseDto)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping(path = "/{conferenceId}")
  public ResponseEntity<UpdateConferenceResponseDto> updateConference(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable UUID conferenceId,
      @Valid @RequestBody UpdateConferenceRequestDto requestDto) {

    UpdateConferenceRequest request = conferenceMapper.fromDto(requestDto);
    UUID organizerId = JwtUtil.parseUserId(jwt);
    Conference conference =
        conferenceService.updateConferenceForOrganizer(organizerId, conferenceId, request);
    UpdateConferenceResponseDto responseDto =
        conferenceMapper.toUpdateConferenceResponseDto(conference);

    return ResponseEntity.ok(responseDto);
  }

  @DeleteMapping(path = "/{conferenceId}")
  public ResponseEntity<Void> deleteConference(
      @AuthenticationPrincipal Jwt jwt, @PathVariable UUID conferenceId) {
    UUID organizerId = JwtUtil.parseUserId(jwt);
    conferenceService.deleteConferenceForOrganizer(organizerId, conferenceId);
    return ResponseEntity.noContent().build();
  }
}
