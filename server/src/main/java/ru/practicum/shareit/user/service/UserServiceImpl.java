package ru.practicum.shareit.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto getById(Long id) {
        return userMapper.toUserDto(userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found")));
    }

    @Override
    public UserDto create(UserDto user) {
        isEmailExist(user.getEmail());

        log.info("Creating new user");
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());

        log.info("Saving new user={}", newUser);
        return userMapper.toUserDto(userRepository.save(newUser));
    }

    @Override
    public UserDto update(UserDto user, Long userId) {
        log.info("Getting user with id={}", userId);
        User oldUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        isEmailExist(user.getEmail());

        log.info("Updating data for user={}", oldUser);
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            oldUser.setEmail(user.getEmail());
        }

        log.info("Calling repository method and returning DTO");
        return userMapper.toUserDto(userRepository.save(oldUser));
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found")));
    }

    private void isEmailExist(String userEmail) {
        log.info("Checking if email already exists");
        if (!userRepository.findByEmailContainingIgnoreCase(userEmail).isEmpty()) {
            throw new EmailConflictException("Email already exists");
        }
    }
}
