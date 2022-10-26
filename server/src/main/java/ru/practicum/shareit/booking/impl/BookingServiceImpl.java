package ru.practicum.shareit.booking.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.common.FromSizeRequest;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.WrongParameterException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public BookingDto create(long bookerId, BookingDto bookingDto) {

        User booker = userRepository.findById(bookerId).orElseThrow();

        Item item = itemRepository.findById(bookingDto.getItem().getId()).orElseThrow();

        //Проверка на доступность вещи
        if (item.getAvailable() == false) {
            throw new BadRequestException("Вещь не доступна для заказа"); //Изменить исключение
        }

        if (item.getOwner().getId() == bookerId) {
            throw new WrongParameterException("Владелец не может у себя арендовать свою вещь");
        }

        Booking booking = BookingMapper.toBooking(bookingDto, item, booker);
        booking.setStatus(Status.WAITING);

        Booking bookingSave = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(bookingSave);
    }

    @Transactional
    @Override
    public BookingDto approved(long userId, long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        Item item = booking.getItem();

        if (item.getOwner().getId() != userId) {
            throw new WrongParameterException("Редактирование не доступно. Нет прав");
        }

        if (approved == true) {
            if (booking.getStatus() == Status.APPROVED) {
                throw new BadRequestException("Статус APPROVED уже установлен");  //Изменить исключение
            } else {
                booking.setStatus(Status.APPROVED);
            }
        } else booking.setStatus(Status.REJECTED);

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Transactional
    @Override
    public BookingDto findBookingDtoById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow();
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new WrongParameterException("Редактирование не доступно. Нет прав");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDto> findAllBookingDtoByBookerId(Long userId, String state, Integer from, Integer size) {

        userRepository.findById(userId).orElseThrow();
        Page<Booking> bookingList;
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = FromSizeRequest.of(from, size, sortById);

        switch (state) {
            case "ALL":
                bookingList = bookingRepository.findAllBookingByBookerIdALL(userId, pageable);
                break;
            case "WAITING":
            case "REJECTED":
                bookingList = bookingRepository.findAllBookingByBookerIdStatus(userId, state, pageable);
                break;
            case "CURRENT":
                bookingList = bookingRepository.findAllBookingByBookerIdCurrent(userId, pageable);
                break;
            case "PAST":
                bookingList = bookingRepository.findAllBookingByBookerIdPast(userId, pageable);
                break;
            default:
                // FUTURE
                bookingList = bookingRepository.findAllBookingByBookerIdFuture(userId, pageable);
        }

        return bookingList
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDto> findAllBookingDtoByOwnerId(Long userId, String state, Integer from, Integer size) {

        userRepository.findById(userId).orElseThrow();

        Page<Booking> bookingList;
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = FromSizeRequest.of(from, size, sortById);

        switch (state) {
            case "ALL":
                bookingList = bookingRepository.findAllBookingByOwnerIdALL(userId, pageable);
                break;
            case "WAITING":
            case "REJECTED":
                bookingList = bookingRepository.findAllBookingByOwnerIdStatus(userId, state, pageable);
                break;
            case "CURRENT":
                bookingList = bookingRepository.findAllBookingByOwnerIdCurrent(userId, pageable);
                break;
            case "PAST":
                bookingList = bookingRepository.findAllBookingByOwnerIdPast(userId, pageable);
                break;
            default:
                // Тогда "FUTURE"
                bookingList = bookingRepository.findAllBookingByOwnerIdFuture(userId, pageable);
        }
        return bookingList
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
