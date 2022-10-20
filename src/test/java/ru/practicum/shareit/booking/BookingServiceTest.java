package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.impl.BookingServiceImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.WrongParameterException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookingServiceTest {


    BookingService bookingService;
    BookingRepository bookingRepository;
    ItemRepository itemRepository;
    UserRepository userRepository;

    User user1;
    User user2;
    Booking booking1;
    Booking booking2;

    Item item1;
    Item item2;


    @BeforeEach
    public void setUp() {
        bookingRepository = Mockito.mock(BookingRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        userRepository = Mockito.mock(UserRepository.class);

        bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);

        user1 = new User(1L, "user1", "user1@email.ru");
        user2 = new User(2L, "user2", "user2@email.ru");

        item1 = new Item(1L, "item1", "desrcription1", true, user1, null);
        item2 = new Item(2L, "item2", "desrcription2", true, user2, null);

        booking1 = new Booking(1L,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(5),
                item1,
                user2,
                Status.WAITING);

        booking2 = new Booking(2L,
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(6),
                item2,
                user1,
                Status.WAITING);
    }

    @Test
    public void findBookingDtoByIdTest() {

        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(booking1));
        BookingDto result = bookingService.findBookingDtoById(booking1.getId(), user1.getId());

        assertThat(result.getId()).isEqualTo(booking1.getId());
    }

    @Test
    public void findBookingDtoByIdWhenUserBadTest() {

        Mockito.when(bookingRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(booking2));

        WrongParameterException exception = assertThrows(WrongParameterException.class, new Executable() {
            @Override
            public void execute() {
                bookingService.findBookingDtoById(booking2.getId(), 3L);
            }
        });

        assertThat(exception.getMessage()).isEqualTo("Редактирование не доступно. Нет прав");
    }
}
