package ru.practicum.statserver.service;

import ru.practicum.statdto.EndpointHitDto;
import ru.practicum.statserver.model.ViewStats;
import ru.practicum.statserver.model.EndpointHit;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;

public interface StatsService {
    EndpointHitDto create(EndpointHit hit);

    Collection<ViewStats> getStats(Timestamp start, Timestamp end, Set<String> uris, Boolean unique);
}
