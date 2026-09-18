package com.example.conference.controllers;

import com.example.conference.services.PassTierService;
import com.example.conference.util.JwtUtil;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/conferences/{conferenceId}/pass-tiers")
@RequiredArgsConstructor
public class PassTierController {

  private final PassTierService passTierService;

  @PostMapping(path = "/{passTierId}/badges")
  public ResponseEntity<Void> purchasePass(
      @AuthenticationPrincipal Jwt jwt,
      @PathVariable UUID conferenceId,
      @PathVariable UUID passTierId) {

    UUID attendeeId = JwtUtil.parseUserId(jwt);
    passTierService.purchasePass(attendeeId, passTierId);
    return ResponseEntity.noContent().build();
  }
}
