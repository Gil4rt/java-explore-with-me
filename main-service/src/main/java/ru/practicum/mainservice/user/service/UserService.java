package ru.practicum.mainservice.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.maindto.UserDto;

import java.util.Collection;

@Service
public interface UserService {
    UserDto create(UserDto userDto);

    void delete(long userId);

    Collection<UserDto> getAll(Collection<Long> ids, int from, int size);
}
