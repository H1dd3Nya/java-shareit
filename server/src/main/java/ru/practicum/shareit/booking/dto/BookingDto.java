package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Item item;
    private User booker;
    private Status status;
}
