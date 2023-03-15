package ru.practicum.statserver.service;

import ru.practicum.statdto.EndpointHitDto;
import ru.practicum.statdto.ViewStatsDto;

import java.util.List;

public interface StatsService {
    EndpointHitDto create(EndpointHitDto hitDto);

    List<ViewStatsDto> getStats(String start, String end, String[] uris, boolean unique);
}
