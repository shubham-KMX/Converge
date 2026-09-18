package com.example.conference.mappers;

import com.example.conference.domain.dtos.GetBadgeResponseDto;
import com.example.conference.domain.dtos.ListBadgePassTierResponseDto;
import com.example.conference.domain.dtos.ListBadgeResponseDto;
import com.example.conference.domain.entities.Badge;
import com.example.conference.domain.entities.PassTier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BadgeMapper {

  ListBadgePassTierResponseDto toListBadgePassTierResponseDto(PassTier passTier);

  ListBadgeResponseDto toListBadgeResponseDto(Badge badge);

  @Mapping(target = "price", source = "passTier.price")
  @Mapping(target = "description", source = "passTier.description")
  @Mapping(target = "conferenceName", source = "passTier.conference.name")
  @Mapping(target = "venue", source = "passTier.conference.venue")
  @Mapping(target = "start", source = "passTier.conference.start")
  @Mapping(target = "end", source = "passTier.conference.end")
  GetBadgeResponseDto toGetBadgeResponseDto(Badge badge);
}
