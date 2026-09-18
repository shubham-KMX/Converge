package com.example.conference.mappers;

import com.example.conference.domain.dtos.SpeakerResponseDto;
import com.example.conference.domain.entities.Speaker;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SpeakerMapper {

  SpeakerResponseDto toSpeakerResponseDto(Speaker speaker);
}
