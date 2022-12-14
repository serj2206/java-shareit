package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.impl.ItemServiceImpl;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ItemServiceImplTest {
    private ItemService itemService;
    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private BookingRepository bookingRepository;
    private CommentRepository commentRepository;
    private ItemRequestRepository itemRequestRepository;
    private User user1;
    private ItemRequest itemRequest1;
    private Item item;

    private Comment comment;
    private CommentDto commentDto;


    @BeforeEach
    public void setUp() {
        itemRepository = Mockito.mock(ItemRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        bookingRepository = Mockito.mock(BookingRepository.class);
        commentRepository = Mockito.mock(CommentRepository.class);
        itemRequestRepository = Mockito.mock(ItemRequestRepository.class);

        itemService = new ItemServiceImpl(itemRepository,
                userRepository,
                bookingRepository,
                commentRepository,
                itemRequestRepository);

        user1 = new User(1L, "user1", "user1@email.ru");

        itemRequest1 = new ItemRequest(1L, "description", user1, LocalDateTime.now());

        item = new Item();
        item.setId(1L);
        item.setName("item1");
        item.setDescription("item1Description1");
        item.setOwner(user1);
        item.setAvailable(true);

        commentDto = new CommentDto();
        commentDto.setText("comment");

        comment = new Comment();
        comment.setId(1L);
        comment.setText("comment");
        comment.setItem(item);
        comment.setAuthor(user1);
        comment.setCreated(LocalDateTime.now());
    }

    @Test
    public void createTest() {
        //Assign
        long userId = 1;
        ItemDto itemDto = new ItemDto();
        itemDto.setName("item1");
        itemDto.setDescription("item1Description1");
        itemDto.setAvailable(true);

        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(user1));
        Mockito.when(itemRequestRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(itemRequest1));
        Mockito.when(itemRepository.save(Mockito.any()))
                .thenReturn(item);

        //Act
        ItemDto result = itemService.create(userId, itemDto);

        //Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("item1");
        assertThat(result.getDescription()).isEqualTo("item1Description1");
        assertThat(result.getAvailable()).isEqualTo(true);
        assertThat(result.getRequestId()).isEqualTo(null);
    }

    @Test
    public void createRequestNotNullTest() {
        //Assign
        long userId = 1;
        ItemDto itemDto = new ItemDto();
        itemDto.setName("item1");
        itemDto.setDescription("item1Description1");
        itemDto.setRequestId(1L);
        itemDto.setAvailable(true);

        Mockito.when(userRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(user1));

        Mockito.when(itemRequestRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(itemRequest1));

        Mockito.when(itemRepository.save(Mockito.any()))
                .thenReturn(item);

        //Act
        item.setRequest(itemRequest1);
        ItemDto result = itemService.create(1L, itemDto);
        //Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("item1");
        assertThat(result.getDescription()).isEqualTo("item1Description1");
        assertThat(result.getAvailable()).isEqualTo(true);
        assertThat(result.getRequestId()).isEqualTo(1L);
    }

    @Test
    public void addCommentTest() {
        //Accept
        Mockito.when(bookingRepository.findByItem_IdAndBooker_IdAndStatus(item.getId(), user1.getId(), "APPROVED"))
                .thenReturn(1L);
        Mockito.when(itemRepository.findById(item.getId()))
                .thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(user1.getId()))
                .thenReturn(Optional.of(user1));
        Mockito.when(commentRepository.save(Mockito.any()))
                .thenReturn(comment);
        //Act
        CommentDto result = itemService.addComment(user1.getId(), item.getId(), commentDto);

        //Assert

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getText()).isEqualTo(comment.getText());
    }

    @Test
    public void addCommentWhenCommentIsNullTest() {
        //Accept
        Mockito.when(bookingRepository.findByItem_IdAndBooker_IdAndStatus(item.getId(), user1.getId(), "APPROVED"))
                .thenReturn(null);

        //Act
        BadRequestException exception = assertThrows(BadRequestException.class, new Executable() {
            @Override
            public void execute() {
                itemService.addComment(user1.getId(), item.getId(), commentDto);
            }
        });

        //Assert
        assertThat(exception.getMessage()).isEqualTo("?????? ???????? ?????? ?????????????????????????????? ???????? ????????");
    }

    @Test
    public void addCommentWhenCommentIsZeroTest() {
        //Accept
        Mockito.when(bookingRepository.findByItem_IdAndBooker_IdAndStatus(item.getId(), user1.getId(), "APPROVED"))
                .thenReturn(0L);

        //Act
        BadRequestException exception = assertThrows(BadRequestException.class, new Executable() {
            @Override
            public void execute() {
                itemService.addComment(user1.getId(), item.getId(), commentDto);
            }
        });

        //Assert
        assertThat(exception.getMessage()).isEqualTo("?????? ???????? ?????? ?????????????????????????????? ???????? ????????");
    }

    @Test
    public void deleteTest() {
        //Accept
        Mockito.when(itemRepository.findById(item.getId()))
                .thenReturn(Optional.of(item));
        //Act
        ItemDto result = itemService.delete(user1.getId(), item.getId());

        //Assert
        assertThat(result.getId()).isEqualTo(item.getId());
    }

}
