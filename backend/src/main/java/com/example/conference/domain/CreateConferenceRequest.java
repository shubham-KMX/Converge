package com.example.conference.domain;

import com.example.conference.domain.entities.ConferenceStatusEnum;
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
public class CreateConferenceRequest {

  private String name;
  private LocalDateTime start;
  private LocalDateTime end;
  private String venue;
  private LocalDateTime salesStart;
  private LocalDateTime salesEnd;
  private ConferenceStatusEnum status;
  private List<CreatePassTierRequest> passTiers = new ArrayList<>();
  private List<CreateSessionRequest> sessions = new ArrayList<>();
}
