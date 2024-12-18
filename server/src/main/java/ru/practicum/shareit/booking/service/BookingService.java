package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {
    Booking create(BookingDto booking, Long userId);

    Booking confirmBooking(Long id, Long userId, Boolean approve);

    Booking get(Long bookingId, Long userId);

    List<Booking> getAllForCurrentUser(Long userId, State state);

    List<Booking> getAllForOwner(Long userId, State state);

    List<Booking> getAllBookingsForUser(Long userId);
}
