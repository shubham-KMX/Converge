package com.example.conference.mappers;

import com.example.conference.domain.dtos.CheckInResponseDto;
import com.example.conference.domain.entities.CheckIn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CheckInMapper {

  @Mapping(target = "badgeId", source = "badge.id")
  CheckInResponseDto toCheckInResponseDto(CheckIn checkIn);
}
