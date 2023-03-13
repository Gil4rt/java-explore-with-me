package ru.practicum.statserver.mapper;

import org.mapstruct.Mapper;
import ru.practicum.statdto.EndpointHitDto;
import ru.practicum.statdto.ViewStatsDto;
import ru.practicum.statserver.model.EndpointHit;
import ru.practicum.statserver.model.ViewStats;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface StatsMapper {

    EndpointHitDto toDTO(EndpointHit model);

    EndpointHit toModel(EndpointHitDto dto);

    ViewStatsDto toDto(ViewStats model);

    Collection<ViewStatsDto> toCollection(Collection<ViewStats> viewStats);

    List<ViewStatsDto> toList(Collection<ViewStats> viewStats);

}
