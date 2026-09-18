package com.example.conference.controllers;

import com.example.conference.domain.dtos.ListSessionResponseDto;
import com.example.conference.mappers.SessionMapper;
import com.example.conference.services.SessionService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/published-conferences/{conferenceId}/sessions")
@RequiredArgsConstructor
public class SessionController {

  private final SessionService sessionService;
  private final SessionMapper sessionMapper;

  @GetMapping
  public List<ListSessionResponseDto> listSessions(@PathVariable UUID conferenceId) {
    return sessionService.listSessionsForConference(conferenceId).stream()
        .map(sessionMapper::toListSessionResponseDto)
        .toList();
  }

  @GetMapping(path = "/{sessionId}")
  public ResponseEntity<ListSessionResponseDto> getSession(
      @PathVariable UUID conferenceId, @PathVariable UUID sessionId) {
    return sessionService.getSession(conferenceId, sessionId)
        .map(sessionMapper::toListSessionResponseDto)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
