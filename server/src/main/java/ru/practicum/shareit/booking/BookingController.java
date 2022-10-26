package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.ValidationException;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @RequestBody BookingDto bookingDto) {
        if (userId <= 0) throw new ValidationException("ID должен быть положительным");
        log.info("BookingController POST createBooking() userId = {}, bookingDto = {}", userId, bookingDto);
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approved(@RequestHeader("X-Sharer-User-Id") long userId,
                               @PathVariable long bookingId,
                               @RequestParam boolean approved) {
        if (userId <= 0) throw new ValidationException("ID должен быть положительным");
        log.info("BookingController PATCH createBooking() userId = {}, bookingId = {}, approved = {}", userId, bookingId, approved);
        return bookingService.approved(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable long bookingId) {
        if (userId <= 0) throw new ValidationException("ID должен быть положительным");
        log.info("BookingController GET findBookingById() userId = {}, bookingId = {}", userId, bookingId);
        return bookingService.findBookingDtoById(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDto> findAllBookingDtoByBooker(@RequestHeader("X-Sharer-User-Id") long userId,
                                                            @RequestParam(defaultValue = "ALL") String state,

                                                            @PositiveOrZero(message = "from не должен быть отрицательным")
                                                            @RequestParam(defaultValue = "1") Integer from,

                                                            @Positive(message = "size должен быть положительным")
                                                            @RequestParam(required = false) Integer size) {
        if (userId <= 0) throw new ValidationException("ID должен быть положительным");
        log.info("BookingController GET findAllBookingDtoByBooker() userId = {}, state = {}, from = {}, size = {}", userId, state, from, size);
        return bookingService.findAllBookingDtoByBookerId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> findAllBookingDtoByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                           @RequestParam(defaultValue = "ALL") String state,

                                                           @PositiveOrZero(message = "from не должен быть отрицательным")
                                                           @RequestParam(defaultValue = "1") Integer from,

                                                           @Positive(message = "size должен быть положительным")
                                                           @RequestParam(required = false) Integer size) {
        if (userId <= 0) throw new ValidationException("ID должен быть положительным");
        log.info("BookingController GET findAllBookingDtoByOwner() userId = {}, state = {}, from = {}, size = {}", userId, state, from, size);
        return bookingService.findAllBookingDtoByOwnerId(userId, state, from, size);
    }
}
