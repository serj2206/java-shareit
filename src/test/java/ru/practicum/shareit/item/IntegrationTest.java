package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.WrongParameterException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Slf4j
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntegrationTest {

    private final ItemService itemService;
    private final UserService userService;

    private final BookingService bookingService;
    private final ItemRequestService itemRequestService;


    @Order(10)
    @Test
    public void createdItemTest() {
        UserDto userDto = new UserDto();
        userDto.setName("user1");
        userDto.setEmail("user1@email.ru");
        UserDto userDtoResult = userService.create(userDto);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("item1");
        itemDto.setDescription("description1");
        itemDto.setAvailable(true);

        ItemDto itemDtoResult = itemService.create(userDtoResult.getId(), itemDto);
        assertThat(itemDtoResult).isNotNull();
        assertThat(itemDtoResult.getId()).isEqualTo(1L);
        assertThat(itemDtoResult.getName()).isEqualTo(itemDto.getName());
        assertThat(itemDtoResult.getDescription()).isEqualTo(itemDto.getDescription());
    }

    @Order(11)
    @Test
    public void createdItemWhenUserIdFalseTest() {

        ItemDto itemDto = new ItemDto();
        itemDto.setName("item2");
        itemDto.setDescription("description2");
        itemDto.setAvailable(true);

        final NoSuchElementException exception =
                assertThrows(NoSuchElementException.class, new Executable() {
                    @Override
                    public void execute() {
                        itemService.create(2L, itemDto);
                    }
                });
        assertThat(exception.getMessage()).isEqualTo("No value present");
    }

    @Order(20)
    @Test
    public void findItemDtoByIdTest() {
        Long userId = 1L;
        Long itemId = 1L;

        ItemDto itemDtoResult = itemService.findItemDtoById(userId, itemId);
        assertThat(itemDtoResult).isNotNull();
        assertThat(itemDtoResult.getId()).isEqualTo(itemId);
    }

    @Order(21)
    @Test
    public void findItemDtoByIdWhenItemIdFalseTest() {
        Long userId = 1L;
        Long itemId = 2L;

        final NoSuchElementException exception =
                assertThrows(NoSuchElementException.class, new Executable() {
                    @Override
                    public void execute() {
                        itemService.findItemDtoById(itemId, userId);
                    }
                });
        assertThat(exception.getMessage()).isEqualTo("No value present");
    }

    @Order(30)
    @Test
    public void findItemAllTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("item2");
        itemDto.setDescription("description2");
        itemDto.setAvailable(true);

        itemService.create(1L, itemDto);
        Collection<ItemDto> resultList = itemService.findItemDtoAll(1L, 0, null);

        assertThat(resultList.size()).isEqualTo(2);
    }

    @Order(31)
    @Test
    public void findItemAllWhenFrom0Size1Test() {
        List<ItemDto> resultList = new ArrayList<>(itemService.findItemDtoAll(1L, 0, 1));

        assertThat(resultList.size()).isEqualTo(1);
        assertThat(resultList.get(0).getId()).isEqualTo(1L);
    }

    @Order(32)
    @Test
    public void findItemAllWhenFromIsNegetiveSize1Test() {
        final BadRequestException exception =
                assertThrows(BadRequestException.class, new Executable() {
                    @Override
                    public void execute() {
                        itemService.findItemDtoAll(1L, -1, 1);
                    }
                });
        assertThat(exception.getMessage()).isEqualTo("from или size имеют отрицательное значение");
    }

    @Order(32)
    @Test
    public void findItemAllWhenFrom1SizeIsNegetiveTest() {
        final BadRequestException exception =
                assertThrows(BadRequestException.class, new Executable() {
                    @Override
                    public void execute() {
                        itemService.findItemDtoAll(1L, 1, -1);
                    }
                });
        assertThat(exception.getMessage()).isEqualTo("from или size имеют отрицательное значение");
    }

    @Order(40)
    @Test
    public void updateTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("item2Update");
        itemDto.setDescription("description2Update");
        itemDto.setAvailable(false);
        ItemDto itemDtoResult = itemService.update(1L, 2L, itemDto);

        assertThat(itemDtoResult).isNotNull();
        assertThat(itemDtoResult.getId()).isEqualTo(2L);
        assertThat(itemDtoResult.getName()).isEqualTo(itemDto.getName());
        assertThat(itemDtoResult.getDescription()).isEqualTo(itemDto.getDescription());
        assertThat(itemDtoResult.getAvailable()).isEqualTo(itemDto.getAvailable());
    }

    @Order(41)
    @Test
    public void updateNameTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("item3Update");
        ItemDto itemDtoResult = itemService.update(1L, 2L, itemDto);

        assertThat(itemDtoResult).isNotNull();
        assertThat(itemDtoResult.getId()).isEqualTo(2L);
        assertThat(itemDtoResult.getName()).isEqualTo(itemDto.getName());
    }

    @Order(42)
    @Test
    public void updateDescriptionTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setDescription("description3Update");
        ItemDto itemDtoResult = itemService.update(1L, 2L, itemDto);

        assertThat(itemDtoResult).isNotNull();
        assertThat(itemDtoResult.getId()).isEqualTo(2L);
        assertThat(itemDtoResult.getDescription()).isEqualTo(itemDto.getDescription());
    }

    @Order(43)
    @Test
    public void updateAvailableTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setAvailable(true);
        ItemDto itemDtoResult = itemService.update(1L, 2L, itemDto);

        assertThat(itemDtoResult).isNotNull();
        assertThat(itemDtoResult.getId()).isEqualTo(2L);
        assertThat(itemDtoResult.getAvailable()).isEqualTo(itemDto.getAvailable());
    }

    @Order(50)
    @Test
    public void findAllTest() {

        UserDto userDto2 = new UserDto();
        userDto2.setName("user2");
        userDto2.setEmail("use2@email.ru");
        userService.create(userDto2);

        List<UserDto> resultList = new ArrayList<>(userService.findAll());

        assertThat(resultList.size()).isEqualTo(2);
    }

    @Order(50)
    @Test
    public void findUserByIdTest() {

        UserDto userDtoResult = userService.findUserById(1L);

        assertThat(userDtoResult.getId()).isEqualTo(1L);
    }

    @Order(60)
    @Test
    public void createdBookingTest() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now().plusDays(1).toString());
        bookingDto.setEnd(LocalDateTime.now().plusDays(5).toString());
        bookingDto.setItemId(1L);

        BookingDto bookingDtoResult = bookingService.create(2L, bookingDto);

        assertThat(bookingDtoResult.getStatus()).isEqualTo(Status.WAITING);

    }

    @Order(61)
    @Test
    public void createdBookingStartBeforeNowTest() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now().minusDays(1).toString());
        bookingDto.setEnd(LocalDateTime.now().plusDays(5).toString());
        bookingDto.setItemId(1L);

        BadRequestException badRequestException =
                assertThrows(BadRequestException.class, new Executable() {
                    @Override
                    public void execute() {
                        bookingService.create(2L, bookingDto);
                    }
                });

        assertThat(badRequestException.getMessage()).isEqualTo("Время бронирования указано неверно: дата начала бронирования указана раньше текущей даты");

    }

    @Order(62)
    @Test
    public void createdBookingStartNullEndNull() {
        BookingDto bookingDto = new BookingDto();

        bookingDto.setItemId(1L);

        BadRequestException badRequestException =
                assertThrows(BadRequestException.class, new Executable() {
                    @Override
                    public void execute() {
                        bookingService.create(2L, bookingDto);
                    }
                });

        assertThat(badRequestException.getMessage()).isEqualTo("Время бронирования указано неверно");

    }

    @Order(63)
    @Test
    public void createdBookingStartAfterEndTest() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now().plusDays(5).toString());
        bookingDto.setEnd(LocalDateTime.now().plusDays(1).toString());
        bookingDto.setItemId(1L);

        BadRequestException badRequestException =
                assertThrows(BadRequestException.class, new Executable() {
                    @Override
                    public void execute() {
                        bookingService.create(2L, bookingDto);
                    }
                });

        assertThat(badRequestException.getMessage())
                .isEqualTo("Время бронирования указано неверно: дата окончания указано раньше, чем дата начала");
    }

    @Order(64)
    @Test
    public void createdBookingOwnerTest() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now().plusDays(1).toString());
        bookingDto.setEnd(LocalDateTime.now().plusDays(5).toString());
        bookingDto.setItemId(1L);

        WrongParameterException exception =
                assertThrows(WrongParameterException.class, new Executable() {
                    @Override
                    public void execute() {
                        bookingService.create(1L, bookingDto);
                    }
                });

        assertThat(exception.getMessage())
                .isEqualTo("Владелец не может у себя арендовать свою вещь");
    }

    @Order(65)
    @Test
    public void createdBookingAvailableFalseTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setAvailable(false);
        itemService.update(1L, 1L, itemDto);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now().plusDays(1).toString());
        bookingDto.setEnd(LocalDateTime.now().plusDays(5).toString());
        bookingDto.setItemId(1L);

        BadRequestException badRequestException =
                assertThrows(BadRequestException.class, new Executable() {
                    @Override
                    public void execute() {
                        bookingService.create(2L, bookingDto);
                    }
                });

        assertThat(badRequestException.getMessage())
                .isEqualTo("Вещь не доступна для заказа");

    }

    @Order(70)
    @Test
    public void findItemRequestByRequestorIdTest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("description");

        ItemRequestDto itemRequestDto1 = itemRequestService.addItemRequest(itemRequestDto, 2L);

        List<ItemRequestDto> list = itemRequestService.findItemRequestByRequestorId(1L, 0, null);

        assertThat(list.size()).isEqualTo(1);
    }

    @Order(71)
    @Test
    public void findItemRequestByRequestorIdFromIsNegativeSizeIsNegative() {

        BadRequestException badRequestException =
                assertThrows(BadRequestException.class, new Executable() {
                    @Override
                    public void execute() {
                        itemRequestService.findItemRequestByRequestorId(1L, -1, 5);
                    }
                });

        assertThat(badRequestException.getMessage())
                .isEqualTo("Значения парметров не корректны");
    }

    @Order(72)
    @Test
    public void findItemRequestByRequestorIdSizeZeroFromOneTest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("descrip");

        itemRequestService.addItemRequest(itemRequestDto, 2L);

        List<ItemRequestDto> list = itemRequestService.findItemRequestByRequestorId(1L, 0, 1);

        assertThat(list.size()).isEqualTo(1);
    }

    @Order(80)
    @Test
    public void approvedBookingTest() {
        BookingDto bookingDtoResult = bookingService.approved(1L, 1L, true);
        assertThat(bookingDtoResult.getStatus()).isEqualTo(Status.APPROVED);
    }

    @Order(81)
    @Test
    public void approvedBookingOwnerTest() {

        WrongParameterException exception =
                assertThrows(WrongParameterException.class, new Executable() {
                    @Override
                    public void execute() {
                        bookingService.approved(2L, 1L, true);
                    }
                });

        assertThat(exception.getMessage())
                .isEqualTo("Редактирование не доступно. Нет прав");
    }

    @Order(82)
    @Test
    public void approvedBookingApprovedRepeatTest() {

        BadRequestException exception =
                assertThrows(BadRequestException.class, new Executable() {
                    @Override
                    public void execute() {
                        bookingService.approved(1L, 1L, true);
                    }
                });

        assertThat(exception.getMessage())
                .isEqualTo("Статус APPROVED уже установлен");
    }

    @Order(83)
    @Test
    public void approvedBookingApprovedFalseTest() {
        BookingDto bookingDtoResult = bookingService.approved(1L, 1L, false);
        assertThat(bookingDtoResult.getStatus()).isEqualTo(Status.REJECTED);

    }



}
