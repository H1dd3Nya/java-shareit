package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Data
public class ItemDto {
    private Long id;
    @Size(min = 2, max = 50)
    private String name;
    @Size(min = 1, max = 255)
    private String description;
    private Boolean available;
    private Booking lastBooking;
    private Booking nextBooking;
    private List<CommentDto> comments;
}
