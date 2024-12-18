package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class RequestDto {
    private Long id;
    @NotNull
    private Long author;
    @NotNull
    @Size(min = 1, max = 255)
    private String description;
}
