package ru.practicum.statserver.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.statdto.EndpointHitDto;
import ru.practicum.statserver.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatsMapper {
    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setApp(endpointHitDto.getApp());
        endpointHit.setIp(endpointHitDto.getIp());
        endpointHit.setUri(endpointHitDto.getUri());
        endpointHit.setTimestamp(LocalDateTime.parse(
                endpointHitDto.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
        return endpointHit;
    }

    public static EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        return new EndpointHitDto(endpointHit.getApp(), endpointHit.getUri(), endpointHit.getIp(), endpointHit.getTimestamp().toString());
    }
}
