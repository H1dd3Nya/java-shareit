package ru.practicum.shareit.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class BookingDateValidator implements ConstraintValidator<BookingDate, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value != null) {
            return value.isAfter(LocalDateTime.now());
        }

        return false;
    }
}
