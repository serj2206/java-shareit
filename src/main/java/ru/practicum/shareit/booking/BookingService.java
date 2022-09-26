package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;


public interface BookingService {

    BookingDto create(long bookerId, BookingDto bookingDto);

    BookingDto approved(long userId, long bookingId, boolean approved);

    BookingDto findBookingDtoById(Long bookingId, Long userId);

    Collection<BookingDto> findAllBookingDtoByBookerId(Long userId, String state);

    Collection<BookingDto> findAllBookingDtoByOwnerId(Long userId, String state);
}
