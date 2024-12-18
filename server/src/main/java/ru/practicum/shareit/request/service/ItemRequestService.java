package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(ItemRequestDto request, Long id);

    ItemRequestDto getItemRequestById(Long id);

    List<ItemRequestDto> getByUserId(Long userId);

    List<ItemRequestDto> getAll();
}
