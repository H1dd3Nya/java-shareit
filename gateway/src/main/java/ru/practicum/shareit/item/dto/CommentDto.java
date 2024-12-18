package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDto {
    @Size(max = 255)
    private String text;
}
