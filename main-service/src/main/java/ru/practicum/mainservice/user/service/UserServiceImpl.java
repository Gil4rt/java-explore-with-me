package ru.practicum.mainservice.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.maindto.UserDto;
import ru.practicum.mainservice.pagination.OffsetPageable;
import ru.practicum.mainservice.user.mapper.UserMapper;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public UserDto create(UserDto userDto) {
        return UserMapper.toUserDto(repository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public void delete(long userId) {
        repository.deleteById(userId);
    }

    @Override
    public Collection<UserDto> getAll(Collection<Long> ids, int from, int size) {
        Page<User> result;
        if (ids == null || ids.isEmpty()) {
            result = repository.findAll(OffsetPageable.newInstance(from, size, Sort.unsorted()));
        } else {
            result = repository.findAllByIdIn(ids, OffsetPageable.newInstance(from, size, Sort.unsorted()));
        }
        return result.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}
