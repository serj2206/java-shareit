package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        log.info("UserController: POST_request create() userDto = {}", userDto);
        return userService.create(userDto);
    }

    @GetMapping
    public Collection<UserDto> findAll() {
        log.info(" UserController: GET_request findAll()");
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public UserDto findUserById(@PathVariable long userId) {
        log.info(" UserController: GET_request, findUserById(), userId = {}", userId);
        return userService.findUserById(userId);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable long userId,
                          @RequestBody UserDto userDto) {
        log.info(" UserController: PATCH_request, update() usrId = {}, userDto = {}", userId, userDto);
        return userService.update(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    public UserDto delete(@PathVariable long userId) {
        log.info(" UserController: DELETE_request с userId = {}", userId);
        return userService.delete(userId);
    }
}
