package ru.practicum.statserver.mapper;

import org.mapstruct.Mapper;
import ru.practicum.statdto.EndpointHitRequestDto;
import ru.practicum.statdto.EndpointHitResponseDto;
import ru.practicum.statdto.ViewStatsDto;
import ru.practicum.statserver.model.EndpointHit;
import ru.practicum.statserver.model.ViewStats;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface StatsMapper {

    EndpointHit toEndpointHit(EndpointHitRequestDto dto);

    EndpointHitResponseDto toEndpointHitResponseDto(EndpointHit entity);

    Collection<ViewStatsDto> toCollectionViewStatsResponseDto(
            Collection<ViewStats> viewStatsCollection);

    ViewStatsDto toViewStatsResponseDto(ViewStats viewStats);

}
