package ru.practicum.mainservice.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.model.dto.UserDto;
import ru.practicum.mainservice.user.model.dto.UserShortDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    User toUser(UserDto userDto);

    UserShortDto toUserShortDto(User user);

}
