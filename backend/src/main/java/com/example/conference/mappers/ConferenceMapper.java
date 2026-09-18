package com.example.conference.mappers;

import com.example.conference.domain.CreateConferenceRequest;
import com.example.conference.domain.CreatePassTierRequest;
import com.example.conference.domain.CreateSessionRequest;
import com.example.conference.domain.UpdateConferenceRequest;
import com.example.conference.domain.UpdatePassTierRequest;
import com.example.conference.domain.UpdateSessionRequest;
import com.example.conference.domain.dtos.CreateConferenceRequestDto;
import com.example.conference.domain.dtos.CreateConferenceResponseDto;
import com.example.conference.domain.dtos.CreatePassTierRequestDto;
import com.example.conference.domain.dtos.CreatePassTierResponseDto;
import com.example.conference.domain.dtos.CreateSessionRequestDto;
import com.example.conference.domain.dtos.CreateSessionResponseDto;
import com.example.conference.domain.dtos.GetConferenceDetailsPassTierResponseDto;
import com.example.conference.domain.dtos.GetConferenceDetailsResponseDto;
import com.example.conference.domain.dtos.GetConferenceDetailsSessionResponseDto;
import com.example.conference.domain.dtos.GetPublishedConferenceDetailsPassTierResponseDto;
import com.example.conference.domain.dtos.GetPublishedConferenceDetailsResponseDto;
import com.example.conference.domain.dtos.GetPublishedConferenceDetailsSessionResponseDto;
import com.example.conference.domain.dtos.ListConferencePassTierResponseDto;
import com.example.conference.domain.dtos.ListConferenceResponseDto;
import com.example.conference.domain.dtos.ListPublishedConferenceResponseDto;
import com.example.conference.domain.dtos.UpdateConferenceRequestDto;
import com.example.conference.domain.dtos.UpdateConferenceResponseDto;
import com.example.conference.domain.dtos.UpdatePassTierRequestDto;
import com.example.conference.domain.dtos.UpdatePassTierResponseDto;
import com.example.conference.domain.dtos.UpdateSessionRequestDto;
import com.example.conference.domain.dtos.UpdateSessionResponseDto;
import com.example.conference.domain.entities.Conference;
import com.example.conference.domain.entities.PassTier;
import com.example.conference.domain.entities.Session;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConferenceMapper {

  // --- Create request DTO -> domain request ---
  CreateConferenceRequest fromDto(CreateConferenceRequestDto dto);

  CreatePassTierRequest fromDto(CreatePassTierRequestDto dto);

  CreateSessionRequest fromDto(CreateSessionRequestDto dto);

  // --- Update request DTO -> domain request ---
  UpdateConferenceRequest fromDto(UpdateConferenceRequestDto dto);

  UpdatePassTierRequest fromDto(UpdatePassTierRequestDto dto);

  UpdateSessionRequest fromDto(UpdateSessionRequestDto dto);

  // --- Entity -> Create response ---
  CreateConferenceResponseDto toCreateConferenceResponseDto(Conference conference);

  CreatePassTierResponseDto toCreatePassTierResponseDto(PassTier passTier);

  @Mapping(target = "speakerId", source = "speaker.id")
  @Mapping(target = "speakerName", source = "speaker.name")
  CreateSessionResponseDto toCreateSessionResponseDto(Session session);

  // --- Entity -> Update response ---
  UpdateConferenceResponseDto toUpdateConferenceResponseDto(Conference conference);

  UpdatePassTierResponseDto toUpdatePassTierResponseDto(PassTier passTier);

  @Mapping(target = "speakerId", source = "speaker.id")
  @Mapping(target = "speakerName", source = "speaker.name")
  UpdateSessionResponseDto toUpdateSessionResponseDto(Session session);

  // --- Entity -> List response ---
  ListConferenceResponseDto toListConferenceResponseDto(Conference conference);

  ListConferencePassTierResponseDto toListConferencePassTierResponseDto(PassTier passTier);

  // --- Entity -> Get details response ---
  GetConferenceDetailsResponseDto toGetConferenceDetailsResponseDto(Conference conference);

  GetConferenceDetailsPassTierResponseDto toGetConferenceDetailsPassTierResponseDto(
      PassTier passTier);

  @Mapping(target = "speakerId", source = "speaker.id")
  @Mapping(target = "speakerName", source = "speaker.name")
  GetConferenceDetailsSessionResponseDto toGetConferenceDetailsSessionResponseDto(Session session);

  // --- Entity -> Published list/details response ---
  ListPublishedConferenceResponseDto toListPublishedConferenceResponseDto(Conference conference);

  GetPublishedConferenceDetailsResponseDto toGetPublishedConferenceDetailsResponseDto(
      Conference conference);

  GetPublishedConferenceDetailsPassTierResponseDto toGetPublishedConferenceDetailsPassTierResponseDto(
      PassTier passTier);

  @Mapping(target = "speakerName", source = "speaker.name")
  GetPublishedConferenceDetailsSessionResponseDto toGetPublishedConferenceDetailsSessionResponseDto(
      Session session);
}
