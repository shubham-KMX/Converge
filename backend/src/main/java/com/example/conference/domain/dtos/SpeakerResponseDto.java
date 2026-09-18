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
public class SpeakerResponseDto {

  private UUID id;
  private String name;
  private String title;
  private String company;
  private String bio;
}
