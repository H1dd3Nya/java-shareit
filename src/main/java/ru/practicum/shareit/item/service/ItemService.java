package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface ItemService {
    ItemDto getById(Long id, Long userId);

    List<ItemDto> getAll(Long ownerId);

    ItemDto create(ItemDto item, Long ownerId);

    ItemDto update(ItemDto item, Long itemId, Long ownerId);

    List<ItemDto> find(String text);

    CommentDto addComment(Comment comment, Long itemId, Long authorId);
}
