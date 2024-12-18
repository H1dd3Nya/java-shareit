package ru.practicum.shareit.request.model;

import lombok.Data;

@Data
public class ItemResponse {
    private Long requestId;
    private Long itemId;
    private String itemName;
    private Long itemOwnerId;
}
