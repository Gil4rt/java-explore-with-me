package ru.practicum.statserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statdto.EndpointHitDto;
import ru.practicum.statdto.ViewStatsDto;
import ru.practicum.statserver.mapper.StatsMapper;
import ru.practicum.statserver.service.StatsService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
public class StatsController {
    private final StatsService statsService;
    private final StatsMapper statsMapper;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto create(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        return statsService.create(statsMapper.toModel(endpointHitDto));
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam String[] uris,
            @RequestParam(defaultValue = "false") boolean unique) {

        return statsService.getStats(start, end, uris, unique);
    }
}
