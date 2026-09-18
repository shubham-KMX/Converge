package com.example.conference.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePassTierRequestDto {

  @NotBlank(message = "Pass tier name is required")
  private String name;

  @NotNull(message = "Price is required")
  @PositiveOrZero(message = "Price must be zero or greater")
  private Double price;

  private String description;

  private Integer totalAvailable;
}
