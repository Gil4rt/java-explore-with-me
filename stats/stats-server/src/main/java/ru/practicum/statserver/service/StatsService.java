package ru.practicum.statserver.service;

import ru.practicum.statdto.EndpointHitDto;
import ru.practicum.statdto.ViewStatsDto;
import ru.practicum.statserver.model.EndpointHit;
import ru.practicum.statserver.model.ViewStats;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface StatsService {
    EndpointHitDto create(EndpointHit hit);

    List<ViewStatsDto> getStats(String start, String end, String[] uris, boolean unique);
}
