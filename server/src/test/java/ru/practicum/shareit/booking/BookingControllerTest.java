package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    private Booking booking;
    public static final String ENDPOINT = "/bookings";
    public static final String SHARER_ID = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        booking = new Booking();
        booking.setId(1L);

        Item item = new Item();
        item.setId(1L);
        booking.setItem(item);

        User booker = new User();
        booker.setId(1L);
        booking.setBooker(booker);

        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(Status.WAITING);
    }

    @Test
    void createBooking() throws Exception {
        when(bookingService.create(any(), anyLong())).thenReturn(booking);

        mockMvc.perform(post(ENDPOINT)
                        .header(SHARER_ID, 1)
                        .content(mapper.writeValueAsString(booking))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking.getId()))
                .andExpect(jsonPath("$.item.id").value(booking.getItem().getId()))
                .andExpect(jsonPath("$.booker.id").value(booking.getBooker().getId()))
                .andExpect(jsonPath("$.status").value(booking.getStatus().toString()));
        verify(bookingService, times(1)).create(any(), anyLong());
    }

    @Test
    void confirmBooking() throws Exception {
        booking.setStatus(Status.APPROVED);
        when(bookingService.confirmBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(booking);

        mockMvc.perform(patch(ENDPOINT + "/1")
                        .header(SHARER_ID, 1)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(booking.getId()))
                .andExpect(jsonPath("$.item.id").value(booking.getItem().getId()))
                .andExpect(jsonPath("$.booker.id").value(booking.getBooker().getId()))
                .andExpect(jsonPath("$.status").value("APPROVED"));
        verify(bookingService, times(1)).confirmBooking(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    void getBooking() throws Exception {
        when(bookingService.get(anyLong(), anyLong())).thenReturn(booking);

        mockMvc.perform(get(ENDPOINT + "/1")
                        .header(SHARER_ID, 1)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking.getId()))
                .andExpect(jsonPath("$.item.id").value(booking.getItem().getId()))
                .andExpect(jsonPath("$.booker.id").value(booking.getBooker().getId()))
                .andExpect(jsonPath("$.status").value(booking.getStatus().toString()));
        verify(bookingService, times(1)).get(anyLong(), anyLong());
    }

    @Test
    void getByBooker() throws Exception {
        when(bookingService.getAllForCurrentUser(anyLong(), any())).thenReturn(List.of(booking));

        mockMvc.perform(get(ENDPOINT)
                        .header(SHARER_ID, 1)
                        .param("state", State.ALL.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
        verify(bookingService, times(1)).getAllForCurrentUser(anyLong(), any());
    }

    @Test
    void getByOwner() throws Exception {
        when(bookingService.getAllForOwner(anyLong(), any())).thenReturn(List.of(booking));

        mockMvc.perform(get(ENDPOINT + "/owner")
                        .header(SHARER_ID, 1)
                        .param("state", State.ALL.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
        verify(bookingService, times(1)).getAllForOwner(anyLong(), any());
    }
}