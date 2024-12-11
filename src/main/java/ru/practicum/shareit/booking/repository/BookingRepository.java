package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerOrderByStartDesc(User booker);

    List<Booking> findAllByBookerAndStatusOrderByStartDesc(User user, Status status);

    List<Booking> findAllByEndBeforeAndBookerEqualsAndStatusOrderByStartDesc
            (LocalDateTime now, User user, Status status);

    List<Booking> findAllByStartGreaterThanAndBookerEqualsAndStatusOrderByStartDesc
            (LocalDateTime start, User user, Status status);

    List<Booking> findAllByStartLessThanAndEndGreaterThanAndBookerEqualsAndStatusOrderByStartDesc
            (LocalDateTime start, LocalDateTime end, User user, Status status);

    @Query("select b from Booking as b" +
            " join Item as i on b.item.id = i.id" +
            " where i.owner = ?1")
    List<Booking> getAllByItemOwner(User owner);

    @Query("select b from Booking as b" +
            " join Item as i on b.item.id = i.id" +
            " where b.end < ?1 and b.status = ?2 and i.owner = ?3")
    List<Booking> getPastBookingsByItemOwner(LocalDateTime now, Status status, User owner);

    @Query("select b from Booking as b" +
            " join Item as i on b.item.id = i.id" +
            " where b.start > ?1 and b.status = ?2 and i.owner = ?3")
    List<Booking> getFutureBookingsByItemOwner(LocalDateTime now, Status status, User owner);

    @Query("select b from Booking as b" +
            " join Item as i on b.item.id = i.id" +
            " where b.start < ?1 and b.end > ?1 and b.status = ?2 and i.owner = ?3")
    List<Booking> getCurrentBookingsByItemOwner(LocalDateTime now, Status status, User owner);

    @Query("select b from Booking as b" +
            " join Item as i on b.item.id = i.id" +
            " where b.status = ?1 and i.owner = ?2")
    List<Booking> getBookingsByStatusAndItemOwner(Status status, User owner);

    @Query("select b from Booking as b" +
            " where b.item.id in ?1")
    List<Booking> getAllBookingsByItemIds(List<Long> itemIds);

    Booking findByItemAndBookerAndEndLessThan(Item item, User booker, LocalDateTime now);

    Booking findFirstByEndLessThanAndItemIdEquals(LocalDateTime now, Long itemId);

    Booking findFirstByStartGreaterThanAndItemIdEquals(LocalDateTime now, Long itemId);
}
