package com.example.conference.domain.dtos;

import com.example.conference.domain.entities.BadgeStatusEnum;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListBadgeResponseDto {

  private UUID id;
  private BadgeStatusEnum status;
  private ListBadgePassTierResponseDto passTier;
}
