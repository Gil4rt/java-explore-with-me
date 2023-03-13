package ru.practicum.statserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statdto.EndpointHitDto;
import ru.practicum.statdto.ViewStatsDto;
import ru.practicum.statserver.mapper.StatsMapper;
import ru.practicum.statserver.model.ViewStats;
import ru.practicum.statserver.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatsRepository repository;
    private final StatsMapper mapper;

    @Override
    @Transactional
    public EndpointHitDto create(EndpointHitDto hitDto) {
        return mapper.toDTO(repository.save(mapper.toModel(hitDto)));
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
