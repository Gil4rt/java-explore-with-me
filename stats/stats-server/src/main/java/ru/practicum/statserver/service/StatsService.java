package ru.practicum.statserver.service;

import ru.practicum.statdto.EndpointHitResponseDto;
import ru.practicum.statserver.model.EndpointHit;
import ru.practicum.statserver.model.ViewStats;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;

public interface StatsService {
    EndpointHitResponseDto create(EndpointHit hit);

    Collection<ViewStats> getStats(Timestamp start, Timestamp end, Set<String> uris, Boolean unique);
}
