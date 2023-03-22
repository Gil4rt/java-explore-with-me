package ru.practicum.mainservice.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import ru.practicum.mainservice.category.mapper.CategoryMapper;
import ru.practicum.mainservice.event.mapper.EventMapper;
import ru.practicum.mainservice.request.model.Request;
import ru.practicum.mainservice.request.model.dto.ParticipationRequestDto;
import ru.practicum.mainservice.user.mapper.UserMapper;


@Mapper(componentModel = "spring", uses = {
        UserMapper.class, CategoryMapper.class, EventMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RequestMapper {

    @Mapping(target = "requester", source = "requesterId")
    @Mapping(target = "event", source = "event.id")
    ParticipationRequestDto toRequestDto(Request request);

    @Mapping(target = "requester", source = "requesterId")
    @Mapping(target = "event", source = "event.id")
    ParticipationRequestDto toParticipationRequestDto(Request request);
}
