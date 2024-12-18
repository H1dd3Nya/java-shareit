package ru.practicum.shareit.request.dto;

import lombok.Data;

@Data
public class Response {
    private Long itemId;
    private String itemName;
    private Long itemOwnerId;
}
