package ru.practicum.statserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statdto.EndpointHitDto;
import ru.practicum.statserver.mapper.StatsMapper;
import ru.practicum.statserver.model.EndpointHit;
import ru.practicum.statserver.model.ViewStats;
import ru.practicum.statserver.repository.StatsRepository;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository repository;
    private final StatsMapper mapper;

    @Override
    @Transactional
    public EndpointHitDto create(EndpointHit hit) {
        return mapper.toDTO(repository.save(hit));
    }

    public Collection<ViewStats> getStats(Timestamp start, Timestamp end, Set<String> uris, Boolean unique) {
        return repository.findEndpointHitStatsByDatesAndUris(start, end, uris, unique);
    }


}
