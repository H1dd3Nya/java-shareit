package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(of = "id")
public class ItemRequest {
    private Long id;
    @Size(max = 255)
    private String description;
    @NotNull
    private Long requester;
    private Date created;
}
