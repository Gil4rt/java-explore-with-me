package ru.practicum.mainservice.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.maindto.UserDto;
import ru.practicum.mainservice.user.model.User;

import java.util.Collection;
import java.util.Optional;

@Service
public interface UserService {
    UserDto create(UserDto userDto);

    void delete(long userId);

    public Collection<UserDto> getAll(Collection<Long> ids, int from, int size);
}
