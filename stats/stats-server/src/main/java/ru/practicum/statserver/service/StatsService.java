package ru.practicum.statserver.service;

import ru.practicum.statdto.EndpointHitDto;
import ru.practicum.statdto.ViewStats;

import java.util.List;

public interface StatsService {
    EndpointHitDto create(EndpointHitDto hitDto);

    List<ViewStats> getStats(String start, String end, List<String> uris, boolean unique);
}
