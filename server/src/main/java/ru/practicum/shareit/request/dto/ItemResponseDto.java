package ru.practicum.shareit.request.dto;

import lombok.Data;

@Data
public class ItemResponseDto {
    private Long id;
    private String name;
    private Long ownerId;
}
