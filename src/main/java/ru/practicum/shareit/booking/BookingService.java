package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;


public interface BookingService {

    BookingDto create(long bookerId, BookingDto bookingDto);

    BookingDto approved(long userId, long bookingId, boolean approved);

    BookingDto findBookingDtoById(Long bookingId, Long userId);

    List<BookingDto> findAllBookingDtoByBookerId(Long userId, String state, Integer from, Integer size);

    List<BookingDto> findAllBookingDtoByOwnerId(Long userId, String state, Integer from, Integer size);
}
