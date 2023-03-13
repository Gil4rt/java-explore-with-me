package ru.practicum.statserver.service;

import ru.practicum.statdto.EndpointHitDto;
import ru.practicum.statdto.ViewStats;
import ru.practicum.statserver.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface StatsService {
    EndpointHitDto create(EndpointHit hit);

    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, Set<String> uris, Boolean unique);
}
