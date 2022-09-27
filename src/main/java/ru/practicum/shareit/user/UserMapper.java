package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static User toUserCreate(UserDto userDto) {
        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
        return user;
    }

    public static User toUserUpdate(UserDto userDto) {
        User user;

        if (userDto.getName() == null) {
            user = User.builder()
                    .id(userDto.getId())
                    .email(userDto.getEmail())
                    .build();
        } else if (userDto.getEmail() == null) {
            user = User.builder()
                    .id(userDto.getId())
                    .name(userDto.getName())
                    .build();
        } else {
            user = User.builder()
                    .id(userDto.getId())
                    .name(userDto.getName())
                    .email(userDto.getEmail())
                    .build();
        }
        return user;
    }
}
