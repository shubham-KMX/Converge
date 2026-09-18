package com.example.conference.domain;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePassTierRequest {

  private UUID id;
  private String name;
  private Double price;
  private String description;
  private Integer totalAvailable;
}
