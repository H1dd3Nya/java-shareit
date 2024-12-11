package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public Booking createBooking(@RequestBody @Valid BookingDto bookingDto,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("POST create booking: {}", bookingDto);
        return bookingService.create(bookingDto, userId);
    }

    @PatchMapping("/{id}")
    public Booking confirmBooking(@PathVariable("id") Long bookingId,
                                  @RequestHeader("X-Sharer-User-Id") Long userId,
                                  @RequestParam Boolean approved) {
        log.info("PATCH confirm booking: {} from user={}, status={}", bookingId, userId, approved);
        return bookingService.confirmBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking getBooking(@PathVariable("bookingId") Long bookingId,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET booking with id: {}, provided by user: {}", bookingId, userId);
        return bookingService.get(bookingId, userId);
    }

    @GetMapping
    public List<Booking> getByBooker(@RequestParam(required = false) State state,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getAllForCurrentUser(userId, state);
    }

    @GetMapping("/owner")
    public List<Booking> getByOwner(@RequestParam(required = false) State state,
                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getAllForOwner(userId, state);
    }

    @GetMapping("/test")
    public List<Booking> testGetAllBookingsForUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getAllBookingsForUser(userId);
    }
}
