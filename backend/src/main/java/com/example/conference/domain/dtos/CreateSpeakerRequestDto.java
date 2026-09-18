package com.example.conference.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSpeakerRequestDto {

  @NotBlank(message = "Speaker name is required")
  private String name;

  private String title;

  private String company;

  private String bio;
}
