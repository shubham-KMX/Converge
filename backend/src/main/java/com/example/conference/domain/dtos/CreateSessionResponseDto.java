package com.example.conference.domain.dtos;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSessionResponseDto {

  private UUID id;
  private String title;
  private String description;
  private String room;
  private LocalDateTime start;
  private LocalDateTime end;
  private UUID speakerId;
  private String speakerName;
}
