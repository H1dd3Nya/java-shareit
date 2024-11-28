package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item create(Item item);

    Optional<Item> read(Long id);

    List<Item> getAll();

    List<Item> getAllByOwner(Long ownerId);

    Item update(Item item);

    void delete(Item item);

    List<Item> findByName(String text);
}
