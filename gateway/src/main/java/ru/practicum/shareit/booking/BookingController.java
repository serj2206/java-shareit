package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ValidationException;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingClient bookingClient;
    ValidationService validationService = new ValidationService();

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestBody BookingDto bookingDto) {
        if (userId <= 0) {
            throw new ValidationException("ID должен быть положительным");
        }
        validationService.validationBookingDataTime(bookingDto);
        log.info("BookingController POST createBooking() userId = {}, bookingDto = {}", userId, bookingDto);
        return bookingClient.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approved(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @Positive @PathVariable long bookingId,
                                           @RequestParam boolean approved) {
        if (userId <= 0) {
            throw new ValidationException("ID должен быть положительным");
        }
        log.info("BookingController PATCH createBooking() userId = {}, bookingId = {}, approved = {}", userId, bookingId, approved);
        return bookingClient.approved(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @Positive @PathVariable long bookingId) {
        if (userId <= 0) {
            throw new ValidationException("ID должен быть положительным");
        }
        log.info("BookingController GET findBookingById() userId = {}, bookingId = {}", userId, bookingId);
        return bookingClient.findBookingDtoById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllBookingDtoByBooker(@RequestHeader("X-Sharer-User-Id") long userId,
                                                            @RequestParam(name = "state", defaultValue = "all") String stateParam,

                                                            @PositiveOrZero(message = "from не должен быть отрицательным")
                                                            @RequestParam(defaultValue = "0") Integer from,

                                                            @Positive(message = "size должен быть положительным")
                                                            @RequestParam(defaultValue = "10") Integer size) {
        if (userId <= 0) {
            throw new ValidationException("ID должен быть положительным");
        }
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new BadRequestException("Unknown state: " + stateParam));
        log.info("BookingController GET findAllBookingDtoByBooker() userId = {}, state = {}, from = {}, size = {}", userId, state, from, size);

        return bookingClient.findAllBookingDtoByBookerId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllBookingDtoByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                           @RequestParam(name = "state", defaultValue = "all") String stateParam,

                                                           @PositiveOrZero(message = "from не должен быть отрицательным")
                                                           @RequestParam(defaultValue = "0") Integer from,

                                                           @Positive(message = "size должен быть положительным")
                                                           @RequestParam(defaultValue = "10") Integer size) {
        if (userId <= 0) {
            throw new ValidationException("ID должен быть положительным");
        }
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new BadRequestException("Unknown state: " + stateParam));
        log.info("BookingController GET findAllBookingDtoByOwner() userId = {}, state = {}, from = {}, size = {}", userId, state, from, size);
        return bookingClient.findAllBookingDtoByOwnerId(userId, state, from, size);
    }
}
