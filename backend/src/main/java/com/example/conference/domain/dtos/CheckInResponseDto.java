package com.example.conference.domain.dtos;

import com.example.conference.domain.entities.CheckInMethod;
import com.example.conference.domain.entities.CheckInStatusEnum;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInResponseDto {

  private UUID id;
  private UUID badgeId;
  private CheckInStatusEnum status;
  private CheckInMethod checkInMethod;
}
