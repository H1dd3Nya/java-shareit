package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "itemId", source = "booking.item.id")
    BookingDto toBookingDto(Booking booking);

    List<BookingDto> toListBookingDto(List<Booking> bookings);
}
