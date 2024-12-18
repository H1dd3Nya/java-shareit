package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.shareit.annotation.Create;

@Data
public class ItemDto {
    @NotNull(groups = Create.class)
    @Size(min = 2, max = 50)
    private String name;
    @NotNull(groups = Create.class)
    @Size(min = 1, max = 255)
    private String description;
    @NotNull(groups = Create.class)
    private Boolean available;
    private Long requestId;
}
