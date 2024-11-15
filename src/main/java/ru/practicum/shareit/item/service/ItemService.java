package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto read(Long id);

    List<ItemDto> getAll(Long ownerId);

    ItemDto create(ItemDto item, Long ownerId);

    ItemDto update(ItemDto item, Long itemId, Long ownerId);

    List<ItemDto> find(String text);
}
