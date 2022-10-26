package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.BadRequestException;

import java.time.LocalDateTime;

public class ValidationService {
    public boolean validationBookingDataTime(BookingDto bookingDto) {
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            throw new BadRequestException("Время бронирования указано неверно");
        }
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new BadRequestException("Время бронирования указано неверно: дата окончания указано раньше, чем дата начала");
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Время бронирования указано неверно: дата начала бронирования указана раньше текущей даты");
        }
        if (bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Время бронирования указано неверно: дата конца бронирования указана раньше текущей даты");
        }
        return true;
    }
}
