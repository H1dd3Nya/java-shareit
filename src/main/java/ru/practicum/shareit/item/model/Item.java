package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class Item {
    private Long id;
    @NotNull
    private Long owner;
    @Size(max = 60)
    private String name;
    @Size(max = 255)
    private String description;
    private Boolean isAvailable;
    private Long requestId;
}
