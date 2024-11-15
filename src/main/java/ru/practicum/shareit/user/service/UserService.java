package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User read(Long id);

    List<UserDto> getAll();

    UserDto create(UserDto user);

    UserDto update(UserDto user, Long userId);

    void delete(Long id);
}
