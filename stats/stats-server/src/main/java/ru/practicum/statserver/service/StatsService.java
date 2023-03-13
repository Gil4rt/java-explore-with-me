package ru.practicum.statserver.service;

import ru.practicum.statdto.EndpointHitDto;
import ru.practicum.statdto.ViewStatsDto;
import ru.practicum.statserver.model.EndpointHit;

import java.util.List;

public interface StatsService {
    EndpointHitDto create(EndpointHit hit);

    List<ViewStatsDto> getStats(String start, String end, String[] uris, boolean unique);
}
