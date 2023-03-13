package ru.practicum.statserver.mapper;

import org.mapstruct.Mapper;
import ru.practicum.statdto.EndpointHitDto;
import ru.practicum.statserver.model.ViewStats;
import ru.practicum.statdto.ViewStatsDto;
import ru.practicum.statserver.model.EndpointHit;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface StatsMapper {

    EndpointHitDto toDTO(EndpointHit model);

    EndpointHit toModel(EndpointHitDto dto);

    ViewStatsDto toDto(ViewStats model);

    Collection<ViewStatsDto> toCollection(Collection<ViewStats> viewStats);

}
