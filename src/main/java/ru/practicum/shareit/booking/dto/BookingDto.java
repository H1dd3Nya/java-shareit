package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.annotation.BookingDate;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    @NotNull
    @BookingDate
    private LocalDateTime start;
    @NotNull
    @BookingDate
    private LocalDateTime end;
    @NotNull
    private Long itemId;
}
