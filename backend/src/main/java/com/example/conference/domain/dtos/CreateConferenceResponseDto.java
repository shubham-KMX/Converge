package com.example.conference.domain.dtos;

import com.example.conference.domain.entities.ConferenceStatusEnum;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateConferenceResponseDto {

  private UUID id;
  private String name;
  private LocalDateTime start;
  private LocalDateTime end;
  private String venue;
  private LocalDateTime salesStart;
  private LocalDateTime salesEnd;
  private ConferenceStatusEnum status;
  private List<CreatePassTierResponseDto> passTiers;
  private List<CreateSessionResponseDto> sessions;
}
