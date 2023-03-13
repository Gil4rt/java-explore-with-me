package ru.practicum.statserver.mapper;

import ru.practicum.statdto.ViewStatsDto;
import ru.practicum.statserver.model.ViewStats;

import java.util.Collection;
import java.util.stream.Collectors;

public class ViewStatsMapper {

    public static Collection<ViewStatsDto> toCollectionViewStatsResponseDto(
            Collection<ViewStats> viewStatsCollection) {
        return viewStatsCollection.stream()
                .map(ViewStatsMapper::toViewStatsResponseDto)
                .collect(Collectors.toList());
    }

    private static ViewStatsDto toViewStatsResponseDto(ViewStats viewStats) {
        return ViewStatsDto.builder()
                .app(viewStats.getApp())
                .uri(viewStats.getUri())
                .hits(viewStats.getHits())
                .build();
    }
}
