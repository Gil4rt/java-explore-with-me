package ru.practicum.statserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statdto.EndpointHitDto;
import ru.practicum.statdto.ViewStats;
import ru.practicum.statserver.mapper.StatsMapper;
import ru.practicum.statserver.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatsRepository repository;

    @Override
    @Transactional
    public EndpointHitDto create(EndpointHitDto hitDto) {
        return StatsMapper.toEndpointHitDto(repository.save(StatsMapper.toEndpointHit(hitDto)));
    }

    @Override
    public List<ViewStats> getStats(String start, String end, List<String> uris, boolean unique) {
        LocalDateTime startDateTime = LocalDateTime.parse(start, FORMATTER);
        LocalDateTime endDateTime = LocalDateTime.parse(end, FORMATTER);
        List<ViewStats> result;
        if (uris == null || uris.isEmpty()) {
            if (!unique) {
                result = repository.findAllNotUniqueIp(startDateTime, endDateTime);
            } else {
                result = repository.findAllUniqueIp(startDateTime, endDateTime);
            }
        } else {
            if (!unique) {
                result = repository.findNotUniqueIP(startDateTime, endDateTime, uris);
            } else {
                result = repository.findUniqueIp(startDateTime, endDateTime, uris);
            }
        }
        return result;
    }
}
