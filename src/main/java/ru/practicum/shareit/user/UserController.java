package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.marker.Create;
import ru.practicum.shareit.marker.Update;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    private UserDto create(@Validated({Create.class}) @RequestBody UserDto userDto) {
        log.info("POST create {}", userDto);
        return userService.create(userDto);
    }

    @GetMapping
    private List<UserDto> findAll() {
        log.info("UserController: Запрос списка пользователей");
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    private UserDto findUserById(@Validated({Update.class}) @PathVariable long userId) {
        log.info("UserController: Запрос данных пользователя с id = {}", userId);
        if(userId <= 0) throw new ValidationException("ID должен быть положительным");
        return userService.findUserById(userId);
    }

    @PatchMapping("/{userId}")
    private UserDto update( @PathVariable long userId,
                        @Validated({Update.class}) @RequestBody UserDto userDto) {
        log.info("UserController: Запрос на обновление данных пользователя с id = {}  userDto = {}", userId, userDto);
        if(userId <= 0) throw new ValidationException("ID должен быть положительным");
        return userService.update(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    private UserDto delete(@PathVariable long userId) {
        log.info("UserController: Запрос на удаление данных пользователя с id = {}", userId);
        if(userId <= 0) throw new ValidationException("ID должен быть положительным");
        return userService.delete(userId);
    }

}
