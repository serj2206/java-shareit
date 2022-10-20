package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.impl.ItemRequestServiceImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemRequestServiceTest {

    ItemRequestService itemRequestService;
    UserRepository userRepository;
    ItemRequestRepository itemRequestRepository;
    ItemRepository itemRepository;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        itemRepository = Mockito.mock((ItemRepository.class));
        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, userRepository, itemRepository);
    }

    @Test
    public void findItemById() {
        User user1 = new User(1L, "user1", "user1@email.ru");
        ItemRequest itemRequest1
                = new ItemRequest(1L, "description", user1, LocalDateTime.now().minusDays(5));

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));
        Mockito.when(itemRequestRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemRequest1));

        ItemRequestDto result = itemRequestService.findItemById(1L, 1L);

        assertThat(result.getId()).isEqualTo(1L);
    }


    @Test
    public void findItemRequestTest() {
        User user1 = new User(1L, "user1", "user1@email.ru");
        ItemRequest itemRequest1
                = new ItemRequest(1L, "description1", user1, LocalDateTime.now().minusDays(5));
        ItemRequest itemRequest2
                = new ItemRequest(2L, "description2", user1, LocalDateTime.now().minusDays(4));

        Mockito.when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito.when(itemRequestRepository.findAllByRequestorId(Mockito.anyLong()))
                .thenReturn(List.of(itemRequest1, itemRequest2));

        List<ItemRequestDto> result = itemRequestService.findItemRequest(1L);

        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
    }



}
