package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Data
@EqualsAndHashCode(of = "id")
public class Item {
    private Long id;
    @NotNull
    private User owner;
    @Size(max = 60)
    private String name;
    @Size(max = 255)
    private String description;
    private Boolean isAvailable;
    private ItemRequest requestId;
}
