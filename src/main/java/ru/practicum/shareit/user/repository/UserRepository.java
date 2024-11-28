package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> getById(Long id);

    List<User> getAll();

    User create(User user);

    User update(User user);

    void delete(Long id);

    boolean isEmailExist(String email);
}
