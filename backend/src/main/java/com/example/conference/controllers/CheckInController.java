package com.example.conference.controllers;

import com.example.conference.domain.dtos.CheckInRequestDto;
import com.example.conference.domain.dtos.CheckInResponseDto;
import com.example.conference.domain.entities.CheckIn;
import com.example.conference.domain.entities.CheckInMethod;
import com.example.conference.mappers.CheckInMapper;
import com.example.conference.services.CheckInService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/check-ins")
@RequiredArgsConstructor
public class CheckInController {

  private final CheckInService checkInService;
  private final CheckInMapper checkInMapper;

  @PostMapping
  public ResponseEntity<CheckInResponseDto> checkIn(
      @Valid @RequestBody CheckInRequestDto requestDto) {

    CheckIn checkIn;
    if (CheckInMethod.MANUAL.equals(requestDto.getMethod())) {
      checkIn = checkInService.checkInManually(requestDto.getId());
    } else {
      checkIn = checkInService.checkInByQrCode(requestDto.getId());
    }

    return ResponseEntity.ok(checkInMapper.toCheckInResponseDto(checkIn));
  }
}
