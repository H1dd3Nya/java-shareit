package ru.practicum.shareit.booking.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Booking create(BookingDto bookingDto, Long userId) {
        if (!isDateCorrect(bookingDto.getStart(), bookingDto.getEnd())) {
            throw new IllegalArgumentException("Incorrect date");
        }

        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setStatus(Status.WAITING);

        User user = getUser(userId);
        Item item = getItem(bookingDto.getItemId());

        if (!item.getIsAvailable()) {
            throw new IllegalArgumentException("Item is not available");
        }

        booking.setItem(item);
        booking.setBooker(user);

        return bookingRepository.save(booking);
    }

    @Override
    public Booking confirmBooking(Long id, Long userId, Boolean approved) {
        Booking booking = getBooking(id);

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("You are not owner of this item");
        }

        if (approved) {
            booking.setStatus(Status.APPROVED);
            return bookingRepository.save(booking);
        }

        booking.setStatus(Status.REJECTED);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking get(Long bookingId, Long userId) {
        Booking booking = getBooking(bookingId);
        Item item = getItem(booking.getItem().getId());
        User user = getUser(userId);

        if (!booking.getBooker().equals(user) && !item.getOwner().equals(user)) {
            throw new AccessDeniedException("You are not allowed to get this booking");
        }

        return booking;
    }

    @Override
    public List<Booking> getAllForCurrentUser(Long userId, State state) {
        User user = getUser(userId);

        if (state == null) {
            state = State.ALL;
        }

        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case CURRENT:
                return bookingRepository
                        .findAllByStartLessThanAndEndGreaterThanAndBookerEqualsAndStatusOrderByStartDesc(now,
                                now,
                                user,
                                Status.APPROVED);
            case PAST:
                return bookingRepository
                        .findAllByEndBeforeAndBookerEqualsAndStatusOrderByStartDesc(now,
                                user,
                                Status.APPROVED);
            case FUTURE:
                return bookingRepository
                        .findAllByStartGreaterThanAndBookerEqualsAndStatusOrderByStartDesc(now,
                                user,
                                Status.APPROVED);
            case WAITING:
                return bookingRepository.findAllByBookerAndStatusOrderByStartDesc(user, Status.WAITING);
            case REJECTED:
                return bookingRepository.findAllByBookerAndStatusOrderByStartDesc(user, Status.REJECTED);
            default:
                return bookingRepository.findAllByBookerOrderByStartDesc(user);
        }
    }

    @Override
    public List<Booking> getAllForOwner(Long userId, State state) {
        User user = getUser(userId);

        if (state == null) {
            state = State.ALL;
        }

        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case CURRENT:
                return bookingRepository.getCurrentBookingsByItemOwner(now, Status.APPROVED, user);
            case PAST:
                return bookingRepository.getPastBookingsByItemOwner(now, Status.APPROVED, user);
            case FUTURE:
                return bookingRepository.getFutureBookingsByItemOwner(now, Status.APPROVED, user);
            case WAITING:
                return bookingRepository.getBookingsByStatusAndItemOwner(Status.WAITING, user);
            case REJECTED:
                return bookingRepository.getBookingsByStatusAndItemOwner(Status.REJECTED, user);
            default:
                return bookingRepository.getAllByItemOwner(user);
        }
    }

    @Override
    public List<Booking> getAllBookingsForUser(Long userId) {
        return bookingRepository.findAllByBookerOrderByStartDesc(getUser(userId));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }

    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item not found"));
    }

    private Booking getBooking(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Booking not found"));
    }

    private boolean isDateCorrect(LocalDateTime start, LocalDateTime end) {
        return !start.equals(end) && start.isBefore(end);
    }
}
