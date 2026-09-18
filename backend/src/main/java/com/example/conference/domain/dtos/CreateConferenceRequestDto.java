package com.example.conference.domain.dtos;

import com.example.conference.domain.entities.ConferenceStatusEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateConferenceRequestDto {

  @NotBlank(message = "Conference name is required")
  private String name;

  private LocalDateTime start;

  private LocalDateTime end;

  @NotBlank(message = "Venue is required")
  private String venue;

  private LocalDateTime salesStart;

  private LocalDateTime salesEnd;

  @NotNull(message = "Conference status is required")
  private ConferenceStatusEnum status;

  @NotEmpty(message = "At least one pass tier is required")
  @Valid
  private List<CreatePassTierRequestDto> passTiers = new ArrayList<>();

  @Valid
  private List<CreateSessionRequestDto> sessions = new ArrayList<>();
}
