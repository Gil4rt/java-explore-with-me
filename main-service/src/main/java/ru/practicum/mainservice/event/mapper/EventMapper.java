package ru.practicum.mainservice.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.mainservice.category.mapper.CategoryMapper;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.model.Location;
import ru.practicum.mainservice.event.model.dto.EventDto;
import ru.practicum.mainservice.event.model.dto.EventShortDto;
import ru.practicum.mainservice.event.model.dto.LocationDto;
import ru.practicum.mainservice.user.mapper.UserMapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CategoryMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventMapper {

    EventDto toEventDto(Event event);

    @Mapping(target = "confirmedRequests", ignore = true)
    EventShortDto toEventShortDto(Event event);

    LocationDto toLocationDto(Location location);

    Location toLocation(LocationDto locationDto);
}
