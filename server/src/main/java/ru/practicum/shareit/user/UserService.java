package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> findAll();

    UserDto findUserById(long id);

    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto, long userId);

    UserDto delete(long userId);
}
