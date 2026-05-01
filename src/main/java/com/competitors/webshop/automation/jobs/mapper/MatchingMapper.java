package com.competitors.webshop.automation.jobs.mapper;

import com.competitors.webshop.automation.config.MapStructConfig;
import com.competitors.webshop.automation.model.MatchResult;
import com.competitors.webshop.automation.modules.matching.dto.MatchResultDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class)
public interface MatchingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ourProductId", source = "productId")
    @Mapping(target = "competitorProductId", ignore = true)
    @Mapping(target = "failReason", ignore = true)
    @Mapping(target = "competitorPrice", ignore = true)
    @Mapping(target = "recommendedPrice", ignore = true)
    @Mapping(target = "matchedAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    MatchResult toEntity(String productId, MatchResultDto result);
}