package com.example.conference.controllers;

import com.example.conference.domain.dtos.GetBadgeResponseDto;
import com.example.conference.domain.dtos.ListBadgeResponseDto;
import com.example.conference.mappers.BadgeMapper;
import com.example.conference.services.BadgeService;
import com.example.conference.services.QrCodeService;
import com.example.conference.util.JwtUtil;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/badges")
@RequiredArgsConstructor
public class BadgeController {

  private final BadgeService badgeService;
  private final BadgeMapper badgeMapper;
  private final QrCodeService qrCodeService;

  @GetMapping
  public Page<ListBadgeResponseDto> listBadges(
      @AuthenticationPrincipal Jwt jwt, Pageable pageable) {
    UUID attendeeId = JwtUtil.parseUserId(jwt);
    return badgeService.listBadgesForAttendee(attendeeId, pageable)
        .map(badgeMapper::toListBadgeResponseDto);
  }

  @GetMapping(path = "/{badgeId}")
  public ResponseEntity<GetBadgeResponseDto> getBadge(
      @AuthenticationPrincipal Jwt jwt, @PathVariable UUID badgeId) {
    UUID attendeeId = JwtUtil.parseUserId(jwt);
    return badgeService.getBadgeForAttendee(attendeeId, badgeId)
        .map(badgeMapper::toGetBadgeResponseDto)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping(path = "/{badgeId}/qr-codes", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<byte[]> getBadgeQrCode(
      @AuthenticationPrincipal Jwt jwt, @PathVariable UUID badgeId) {
    UUID attendeeId = JwtUtil.parseUserId(jwt);
    byte[] qrCodeImage = qrCodeService.getQrCodeImageForAttendeeAndBadge(attendeeId, badgeId);
    return ResponseEntity.ok()
        .contentType(MediaType.IMAGE_PNG)
        .body(qrCodeImage);
  }
}
