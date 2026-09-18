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
public class ListPublishedConferenceResponseDto {

  private UUID id;
  private String name;
  private LocalDateTime start;
  private LocalDateTime end;
  private String venue;
}
