package com.example.conference.domain.dtos;

import com.example.conference.domain.entities.ConferenceStatusEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class UpdateConferenceRequestDto {

  @NotNull(message = "Conference ID is required")
  private UUID id;

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
  private List<UpdatePassTierRequestDto> passTiers = new ArrayList<>();

  @Valid
  private List<UpdateSessionRequestDto> sessions = new ArrayList<>();
}
