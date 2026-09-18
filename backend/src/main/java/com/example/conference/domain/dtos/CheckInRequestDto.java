package com.example.conference.domain.dtos;

import com.example.conference.domain.entities.CheckInMethod;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInRequestDto {

  @NotNull(message = "ID is required")
  private UUID id;

  @NotNull(message = "Check-in method is required")
  private CheckInMethod method;
}
