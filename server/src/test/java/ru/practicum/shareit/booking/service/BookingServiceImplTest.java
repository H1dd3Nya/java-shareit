package ru.practicum.shareit.booking.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.TestValuesGenerator;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class BookingServiceImplTest extends TestValuesGenerator {
    private final BookingService service;

    public BookingServiceImplTest(@Autowired EntityManager em, @Autowired BookingService service) {
        super(em);
        this.service = service;
    }

    @Test
    void create_returnNewBooking() {
        setTestEntities();
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));

        Booking booking = service.create(bookingDto, 2L);
        Booking testBooking = getTestBooking(booking.getId());

        assertEqualsBookings(testBooking, booking);
    }

    @Test
    void create_IllegalArgumentInDate() {
        setTestEntities();
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().minusDays(1));

        assertThrows(IllegalArgumentException.class, () -> {
            service.create(bookingDto, 2L);
        });
    }

    @Test
    void create_IllegalArgumentUnavailableItem() {
        setTestUser(1);
        setTestUser(2);
        setTestItem(1, 1, false, null);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().minusDays(1));

        assertThrows(IllegalArgumentException.class, () -> {
            service.create(bookingDto, 2L);
        });
    }

    @Test
    void confirmBooking_returnApprovedBooking() {
        setTestEntities();
        setTestBooking(Status.WAITING, 1L);
        Booking testBooking = getTestBooking(1L);
        testBooking.setStatus(Status.APPROVED);

        Booking booking = service.confirmBooking(1L, 1L, true);

        assertEqualsBookings(testBooking, booking);
    }

    @Test
    void confirmBooking_AccessDenied() {
        setTestEntities();
        setTestBooking(Status.WAITING, 1L);
        Booking testBooking = getTestBooking(1L);
        testBooking.setStatus(Status.APPROVED);

        assertThrows(AccessDeniedException.class, () -> {
            service.confirmBooking(1L, 2L, false);
        });
    }

    @Test
    void getBooking_returnBooking() {
        setTestEntities();
        setTestBooking(Status.WAITING, 1L);
        Booking testBooking = getTestBooking(1L);

        Booking booking = service.get(1L, 2L);

        assertEqualsBookings(testBooking, booking);
    }

    @Test
    void getBooking_AccessDenied() {
        setTestEntities();
        setTestUser(3);
        setTestBooking(Status.WAITING, 1L);

        assertThrows(AccessDeniedException.class, () -> {
            service.get(1L, 3L);
        });
    }

    @Test
    void getAllForCurrentUser_returnAllBookingsList() {
        setTestEntities();
        List<Booking> testBooking = getAllTestBookings();

        List<Booking> bookings = service.getAllForCurrentUser(2L, State.ALL);

        assertEquals(testBooking.size(), bookings.size());
    }

    @Test
    void getAllForCurrentUser_returnWaitingBookingsList() {
        setTestEntities();
        setTestBooking(Status.WAITING, 1L);
        setTestBooking(Status.WAITING, 2L);
        List<Booking> testBooking = getAllTestBookings();

        List<Booking> bookings = service.getAllForCurrentUser(2L, State.WAITING);

        assertEquals(testBooking.size(), bookings.size());
    }

    @Test
    void getAllForCurrentUser_returnFutureBookingsList() {
        setTestEntities();
        setTestBooking(1, 1, 2, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        setTestBooking(2, 1, 2, LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4));
        service.confirmBooking(1L, 1L, true);
        service.confirmBooking(2L, 1L, true);
        List<Booking> testBooking = getAllTestBookings();

        List<Booking> bookings = service.getAllForCurrentUser(2L, State.FUTURE);

        assertEquals(testBooking.size(), bookings.size());
    }

    @Test
    void getAllForCurrentUser_returnPastBookingsList() {
        setTestEntities();
        setTestBooking(1, 1, 2, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(2));
        setTestBooking(2, 1, 2, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(4));
        service.confirmBooking(1L, 1L, true);
        service.confirmBooking(2L, 1L, true);
        List<Booking> testBooking = getAllTestBookings();

        List<Booking> bookings = service.getAllForCurrentUser(2L, State.PAST);

        assertEquals(testBooking.size(), bookings.size());
    }

    @Test
    void getAllForCurrentUser_returnCurrentBookingsList() {
        setTestEntities();
        setTestBooking(1, 1, 2, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(2));
        setTestBooking(2, 1, 2, LocalDateTime.now().minusDays(3), LocalDateTime.now().plusDays(4));
        service.confirmBooking(1L, 1L, true);
        service.confirmBooking(2L, 1L, true);
        List<Booking> testBooking = getAllTestBookings();

        List<Booking> bookings = service.getAllForCurrentUser(2L, State.CURRENT);

        assertEquals(testBooking.size(), bookings.size());
    }

    @Test
    void getAllForCurrentUser_returnRejectedBookingsList() {
        setTestEntities();
        setTestBooking(1, 1, 2, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(2));
        setTestBooking(2, 1, 2, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(4));
        service.confirmBooking(1L, 1L, false);
        service.confirmBooking(2L, 1L, false);
        List<Booking> testBooking = getAllTestBookings();

        List<Booking> bookings = service.getAllForCurrentUser(2L, State.REJECTED);

        assertEquals(testBooking.size(), bookings.size());
    }

    @Test
    void getAllForOwner_returnOwnerBookingsList() {
        setTestEntities();
        setTestBooking(Status.WAITING, 1L);
        setTestBooking(Status.WAITING, 2L);
        List<Booking> testBooking = getAllTestBookings();

        List<Booking> bookings = service.getAllForOwner(1L, State.ALL);

        assertEquals(testBooking.size(), bookings.size());
    }

    @Test
    void getAllForOwner_returnWaitingBookingsList() {
        setTestEntities();
        setTestBooking(Status.WAITING, 1L);
        setTestBooking(Status.WAITING, 2L);
        List<Booking> testBooking = getAllTestBookings();

        List<Booking> bookings = service.getAllForOwner(1L, State.WAITING);

        assertEquals(testBooking.size(), bookings.size());
    }

    @Test
    void getAllForOwner_returnFutureBookingsList() {
        setTestEntities();
        setTestBooking(1, 1, 2, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        setTestBooking(2, 1, 2, LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4));
        service.confirmBooking(1L, 1L, true);
        service.confirmBooking(2L, 1L, true);
        List<Booking> testBooking = getAllTestBookings();

        List<Booking> bookings = service.getAllForOwner(1L, State.FUTURE);

        assertEquals(testBooking.size(), bookings.size());
    }

    @Test
    void getAllForOwner_returnPastBookingsList() {
        setTestEntities();
        setTestBooking(1, 1, 2, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(2));
        setTestBooking(2, 1, 2, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(4));
        service.confirmBooking(1L, 1L, true);
        service.confirmBooking(2L, 1L, true);
        List<Booking> testBooking = getAllTestBookings();

        List<Booking> bookings = service.getAllForOwner(1L, State.PAST);

        assertEquals(testBooking.size(), bookings.size());
    }

    @Test
    void getAllForOwner_returnCurrentBookingsList() {
        setTestEntities();
        setTestBooking(1, 1, 2, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(2));
        setTestBooking(2, 1, 2, LocalDateTime.now().minusDays(3), LocalDateTime.now().plusDays(4));
        service.confirmBooking(1L, 1L, true);
        service.confirmBooking(2L, 1L, true);
        List<Booking> testBooking = getAllTestBookings();

        List<Booking> bookings = service.getAllForOwner(1L, State.CURRENT);

        assertEquals(testBooking.size(), bookings.size());
    }

    @Test
    void getAllForOwner_returnRejectedBookingsList() {
        setTestEntities();
        setTestBooking(1, 1, 2, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(2));
        setTestBooking(2, 1, 2, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(4));
        service.confirmBooking(1L, 1L, false);
        service.confirmBooking(2L, 1L, false);
        List<Booking> testBooking = getAllTestBookings();

        List<Booking> bookings = service.getAllForOwner(1L, State.REJECTED);

        assertEquals(testBooking.size(), bookings.size());
    }

    protected void assertEqualsBookings(Booking expected, Booking actual) {
        assertEquals(expected.getStart(), actual.getStart());
        assertEquals(expected.getEnd(), actual.getEnd());

        assertEquals(expected.getItem().getId(), actual.getItem().getId());
        assertEquals(expected.getItem().getName(), actual.getItem().getName());
        assertEquals(expected.getItem().getDescription(), actual.getItem().getDescription());
        assertEquals(expected.getItem().getIsAvailable(), actual.getItem().getIsAvailable());

        assertEquals(expected.getBooker().getId(), actual.getBooker().getId());
        assertEquals(expected.getBooker().getName(), actual.getBooker().getName());
        assertEquals(expected.getBooker().getEmail(), actual.getBooker().getEmail());
    }
}