package ru.practicum.shareit.booking.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.ValidationService;
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

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ValidationService validationService = new ValidationService();

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public BookingDto create(long bookerId, BookingDto bookingDto) {
        //Проверка временных интервалов
        validationService.validationBookingDataTime(bookingDto);

        User booker = userRepository.findById(bookerId).orElseThrow();

        Item item = itemRepository.findById(bookingDto.getItem().getId()).orElseThrow();
        //Проверка на доступность вещи
        if (item.getAvailable() == false) {
            throw new BadRequestException("Вещь не доступна для заказа");
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
                throw new BadRequestException("Статус APPROVED уже установлен");
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
    public Collection<BookingDto> findAllBookingDtoByBookerId(Long userId, String state, Integer from, Integer size) {
        if (size != null && (from < 0 || size < 0)) {
            throw new BadRequestException("from или size имеют отрицательное значение");
        }

        userRepository.findById(userId).orElseThrow();
        Page<Booking> bookingList;
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable;

        if (size == null) {
            pageable = null;
        } else {
            pageable = FromSizeRequest.of(from, size, sortById);
        }

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
            case "FUTURE":
                bookingList = bookingRepository.findAllBookingByBookerIdFuture(userId, pageable);
                break;
            default:
                throw new BadRequestException("Unknown state: " + state);
        }

        return bookingList
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<BookingDto> findAllBookingDtoByOwnerId(Long userId, String state, Integer from, Integer size) {

        if (size != null && (from < 0 || size < 0)) {
            throw new BadRequestException("from или size имеют отрицательное значение");
        }

        userRepository.findById(userId).orElseThrow();

        Page<Booking> bookingList;
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable;

        if (size == null) {
            pageable = null;
        } else {
            pageable = FromSizeRequest.of(from, size, sortById);
        }

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
            case "FUTURE":
                bookingList = bookingRepository.findAllBookingByOwnerIdFuture(userId, pageable);
                break;
            default:
                throw new BadRequestException("Unknown state: " + state);
        }
        return bookingList
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
