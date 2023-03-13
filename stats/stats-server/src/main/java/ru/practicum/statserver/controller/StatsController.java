package ru.practicum.statserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statdto.EndpointHitRequestDto;
import ru.practicum.statdto.EndpointHitResponseDto;
import ru.practicum.statdto.ViewStatsDto;
import ru.practicum.statserver.mapper.StatsMapper;
import ru.practicum.statserver.service.StatsService;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;
    private final StatsMapper statsMapper;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitResponseDto create(
            @RequestBody @Valid EndpointHitRequestDto endpointHitRequestDto) {

        return statsService.create(statsMapper.toEndpointHit(endpointHitRequestDto));
    }

    @GetMapping("/stats")
    public Collection<ViewStatsDto> getStats(
            @RequestParam Timestamp start,
            @RequestParam Timestamp end,
            @RequestParam Set<String> uris,
            @RequestParam(defaultValue = "false") boolean unique) {

        return statsMapper.toCollectionViewStatsResponseDto(
                statsService.getStats(start, end, uris, unique));
    }
}
