package com.example.conference.domain.dtos;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListBadgePassTierResponseDto {

  private UUID id;
  private String name;
  private Double price;
}
