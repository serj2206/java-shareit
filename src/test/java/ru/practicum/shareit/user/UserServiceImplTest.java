package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import ru.practicum.shareit.exception.WrongParameterException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.impl.UserServiceImpl;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;


public class UserServiceImplTest {
    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    //Тест проверка маппера
    @Test
    public void createdTest() {
        //Assign
        UserDto userDto1 = new UserDto();
        userDto1.setName("user1");
        userDto1.setEmail("user1@email.ru");

        Mockito.when(userRepository.save(any()))
                .thenReturn(new User(1L, "user1", "user1@email.ru"));

        //Act
        UserDto result = userService.create(userDto1);

        //Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("user1", result.getName());
        assertEquals("user1@email.ru", result.getEmail());
    }

    //Обновление данных: нет данных
    @Test
    void updateNameNullEmailNullTest() {
        //Assign
        UserDto userDto1 = new UserDto();
        long id = 1L;
        //Act
        final WrongParameterException exception =
                assertThrows(WrongParameterException.class, new Executable() {
                    @Override
                    public void execute() {
                        userService.update(userDto1, id);
                    }
                });

        //Assert
        assertEquals("Нет данных для обновления", exception.getMessage());
    }

    //Обновление данных: нет имени
    @Test
    void updateNameNullTest() {
        //Assign
        UserDto userDto1 = new UserDto();
        userDto1.setEmail("user1@emailUpdate.ru");

        long userId = 1L;

        //Act
        Mockito.when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User(1L, "user1", "user1@email.ru")));
        Mockito.when(userRepository.save(any()))
                .thenReturn(new User(1L, "user1", "user1@emailUpdate.ru"));

        //Assert
        UserDto result = userService.update(userDto1, 1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("user1", result.getName());
        assertEquals("user1@emailUpdate.ru", result.getEmail());
    }

    //Обновление данных: нет email
    @Test
    void updateEmailNullTest() {
        //Assign
        UserDto userDto1 = new UserDto();
        userDto1.setName("user1Update.ru");

        long userId = 1L;

        //Act
        Mockito.when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User(1L, "user1", "user1@email.ru")));
        Mockito.when(userRepository.save(any()))
                .thenReturn(new User(1L, "user1Update", "user1@email.ru"));

        //Assert
        UserDto result = userService.update(userDto1, 1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("user1Update", result.getName());
        assertEquals("user1@email.ru", result.getEmail());
    }

    //Обновление данных:
    @Test
    void updateTest() {
        //Assign
        UserDto userDto1 = new UserDto();
        userDto1.setName("user1Update.ru");
        userDto1.setEmail("user1@emailUpdate.ru");

        long userId = 1L;

        //Act
        Mockito.when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(new User(1L, "user1", "user1@email.ru")));
        Mockito.when(userRepository.save(any()))
                .thenReturn(new User(1L, "user1Update", "user1@emailUpdate.ru"));

        //Assert
        UserDto result = userService.update(userDto1, 1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("user1Update", result.getName());
        assertEquals("user1@emailUpdate.ru", result.getEmail());
    }
}
