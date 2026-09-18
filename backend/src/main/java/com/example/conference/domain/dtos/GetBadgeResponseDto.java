package com.example.conference.domain.dtos;

import com.example.conference.domain.entities.BadgeStatusEnum;
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
public class GetBadgeResponseDto {

  private UUID id;
  private BadgeStatusEnum status;
  private Double price;
  private String description;
  private String conferenceName;
  private String venue;
  private LocalDateTime start;
  private LocalDateTime end;
}
