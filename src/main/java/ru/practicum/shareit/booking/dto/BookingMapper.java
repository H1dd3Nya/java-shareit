package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.Booking;

import java.util.List;

@Mapper
public interface BookingMapper {
    BookingDto toBookingDto(Booking booking);

    List<BookingDto> toListBookingDto(List<Booking> bookings);
}
