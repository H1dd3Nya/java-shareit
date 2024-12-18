package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.annotation.Create;
import ru.practicum.shareit.user.dto.UserDto;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Validated(Create.class) UserDto user) {
        log.info("POST create with body={}", user);
        return userClient.createUser(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        log.info("GET read with id={}", id);
        return userClient.getUser(id);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@RequestBody @Valid UserDto user,
                                         @PathVariable Long userId) {
        log.info("PATCH update with userId={} and body={}", userId, user);
        return userClient.updateUser(userId, user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        log.info("DELETE delete with id={}", id);
        return userClient.deleteUser(id);
    }
}
