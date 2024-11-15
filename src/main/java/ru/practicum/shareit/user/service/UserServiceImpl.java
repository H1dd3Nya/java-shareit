package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final List<String> emailCache = new LinkedList<>();
    private int cacheCounter = 0;

    @Override
    public User read(Long id) {
        return userRepository.read(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public List<UserDto> getAll() {
        return userMapper.toUserDtoList(userRepository.getAll());
    }

    @Override
    public UserDto create(UserDto user) {
        isEmailExist(user.getEmail());

        log.info("Creating new user");
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());

        log.info("Saving new user={}", newUser);
        return userMapper.toUserDto(userRepository.create(newUser));
    }

    @Override
    public UserDto update(UserDto user, Long userId) {
        isEmailExist(user.getEmail());

        log.info("Getting user with id={}", userId);
        User oldUser = userRepository.read(userId).orElseThrow(() -> new NotFoundException("User not found"));

        log.info("Updating data for user={}", oldUser);
        oldUser.setName(user.getName());
        oldUser.setEmail(user.getEmail());

        log.info("Calling repository method and returning DTO");
        return userMapper.toUserDto(userRepository.update(oldUser));
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }

    private void isEmailExist(String userEmail) {
        log.info("Checking if email already exists");
        if (cacheCounter == 5) {
            updateCache(userRepository.getAll().stream().map(User::getEmail).toList());

            for (String cachedEmail : emailCache) {
                if (cachedEmail != null && cachedEmail.equals(userEmail)) {
                    throw new EmailConflictException("Email already exists");
                }
            }

            return;
        }

        cacheCounter++;
        List<String> emails = userRepository.getAll().stream().map(User::getEmail).toList();

        for (String email : emails) {
            if (email != null && email.equals(userEmail)) {
                throw new EmailConflictException("Email already exists");
            }
        }
    }

    private void updateCache(List<String> emails) {
        emailCache.clear();
        emailCache.addAll(emails);
    }
}
