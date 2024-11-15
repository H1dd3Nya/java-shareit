package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto create(@RequestBody @Valid UserDto user) {
        log.info("POST create with body={}", user);
        return userService.create(user);
    }

    @GetMapping("/{id}")
    public User read(@PathVariable Long id) {
        log.info("GET read with id={}", id);
        return userService.read(id);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto user,
                          @PathVariable Long userId) {
        log.info("PATCH update with userId={} and body={}", userId, user);
        return userService.update(user, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("DELETE delete with id={}", id);
        userService.delete(id);
    }
}
