package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") long bookerId,
                                    @RequestBody BookingDto bookingDto) {
        log.info("BookingController POST createBooking() userId = {}, bookingDto = {}", bookerId, bookingDto);
        return bookingService.create(bookerId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approved(@RequestHeader("X-Sharer-User-Id") long userId,
                               @PathVariable long bookingId,
                               @RequestParam boolean approved) {
        log.info("BookingController PATCH createBooking() userId = {}, bookingId = {}, approved = {}", userId, bookingId, approved);
        return bookingService.approved(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable long bookingId) {
        log.info("BookingController GET findBookingById() userId = {}, bookingId = {}", userId, bookingId);
        return bookingService.findBookingDtoById(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDto> findAllBookingDtoByBooker(@RequestHeader("X-Sharer-User-Id") long userId,
                                                            @RequestParam(defaultValue = "ALL") String state,
                                                            @RequestParam(defaultValue = "1") Integer from,
                                                            @RequestParam(required = false) Integer size) {
        log.info("BookingController GET findAllBookingDtoByBooker() userId = {}, state = {}, from = {}, size = {}", userId, state, from, size);
        return bookingService.findAllBookingDtoByBookerId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> findAllBookingDtoByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                           @RequestParam(defaultValue = "ALL") String state,
                                                           @RequestParam(defaultValue = "1") Integer from,
                                                           @RequestParam(required = false) Integer size) {
        log.info("BookingController GET findAllBookingDtoByOwner() userId = {}, state = {}, from = {}, size = {}", userId, state, from, size);
        return bookingService.findAllBookingDtoByOwnerId(userId, state, from, size);
    }
}
