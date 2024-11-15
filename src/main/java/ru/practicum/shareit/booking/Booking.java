package ru.practicum.shareit.booking;

import lombok.Data;

import java.util.Date;

@Data
public class Booking {
    private Long id;
    private Long itemId;
    private Date startDate;
    private Date endDate;
    private Long booker;
    private Status status;
}
