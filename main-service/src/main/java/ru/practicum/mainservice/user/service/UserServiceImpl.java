package ru.practicum.mainservice.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mainservice.exception.ConflictException;
import ru.practicum.mainservice.pagination.OffsetPageable;
import ru.practicum.mainservice.user.mapper.UserMapper;
import ru.practicum.mainservice.user.model.User;
import ru.practicum.mainservice.user.model.dto.UserDto;
import ru.practicum.mainservice.user.repository.UserRepository;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    public UserDto create(UserDto userDto) {
        validateUserDto(userDto);
        User user = mapper.toUser(userDto);
        User savedUser = userRepository.save(user);
        return mapper.toUserDto(savedUser);
    }

    @Override
    public void delete(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ConflictException("User with ID " + userId + " does not exist"));
        userRepository.delete(user);
    }

    @Override
    public Collection<UserDto> getAll(Collection<Long> ids, int from, int size) {
        Page<User> result = ids == null || ids.isEmpty()
                ? userRepository.findAll(OffsetPageable.newInstance(from, size, Sort.unsorted()))
                : userRepository.findAllByIdIn(ids, OffsetPageable.newInstance(from, size, Sort.unsorted()));
        return result.stream().map(mapper::toUserDto).collect(Collectors.toList());
    }

    private void validateUserDto(UserDto userDto) {
        if (userDto.getName() == null) {
            throw new ValidationException("Name cannot be null");
        }
        if (userRepository.findByName(userDto.getName()).isPresent()) {
            throw new ConflictException("This name is already taken");
        }
    }
}
