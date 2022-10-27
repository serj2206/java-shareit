package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.marker.Create;
import ru.practicum.shareit.marker.Update;
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserClient userClient;


    @PostMapping
    public ResponseEntity<Object> create(@Validated({Create.class}) @RequestBody UserDto userDto) {
        log.info("UserController: POST_request create() userDto = {}", userDto);
        return userClient.create(userDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        log.info("UserController: GET_request findAll()");
        return userClient.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findUserById(@Validated({Update.class}) @PathVariable long userId) {
        log.info("UserController: GET_request, findUserById(), userId = {}", userId);
        if (userId <= 0) {
            throw new ValidationException("ID должен быть положительным");
        }
        return userClient.findUserById(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable long userId,
                        @Validated({Update.class}) @RequestBody UserDto userDto) {
        log.info("UserController: PATCH_request, update() usrId = {}, userDto = {}", userId, userDto);
        if (userId <= 0) {
            throw new ValidationException("ID должен быть положительным");
        }
        return userClient.update(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable long userId) {
        log.info("UserController: DELETE_request с userId = {}", userId);
        if (userId <= 0) {
            throw new ValidationException("ID должен быть положительным");
        }
        return userClient.delete(userId);
    }
}
