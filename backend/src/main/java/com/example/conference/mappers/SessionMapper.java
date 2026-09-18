package com.example.conference.mappers;

import com.example.conference.domain.dtos.ListSessionResponseDto;
import com.example.conference.domain.entities.Session;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SessionMapper {

  @Mapping(target = "speakerId", source = "speaker.id")
  @Mapping(target = "speakerName", source = "speaker.name")
  ListSessionResponseDto toListSessionResponseDto(Session session);
}
