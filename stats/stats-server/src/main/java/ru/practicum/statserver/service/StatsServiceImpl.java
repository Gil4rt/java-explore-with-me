package ru.practicum.statserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statdto.EndpointHitDto;
import ru.practicum.statdto.ViewStatsDto;
import ru.practicum.statserver.mapper.StatsMapper;
import ru.practicum.statserver.model.EndpointHit;
import ru.practicum.statserver.model.ViewStats;
import ru.practicum.statserver.repository.StatsRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository repository;
    private final StatsMapper mapper;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional
    public EndpointHitDto create(EndpointHit hit) {
        return mapper.toDTO(repository.save(hit));
    }
    @Override
    public List<ViewStatsDto> getStats(String start, String end, String[] uris, boolean unique) {
        LocalDateTime startDateTime = LocalDateTime.parse(start, FORMATTER);
        LocalDateTime endDateTime = LocalDateTime.parse(end, FORMATTER);
        List<ViewStats> result;
        if (!unique) {
            result = repository.findNotUniqueIP(startDateTime, endDateTime, List.of(uris));
        } else {
            result = repository.findUniqueIp(startDateTime, endDateTime, List.of(uris));
        }
        return mapper.toList(result);
    }


}
