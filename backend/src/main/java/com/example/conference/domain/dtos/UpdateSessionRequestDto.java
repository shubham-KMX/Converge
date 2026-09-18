package com.example.conference.domain.dtos;

import jakarta.validation.constraints.NotBlank;
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
public class UpdateSessionRequestDto {

  private UUID id;

  @NotBlank(message = "Session title is required")
  private String title;

  private String description;

  private String room;

  private LocalDateTime start;

  private LocalDateTime end;

  private UUID speakerId;
}
