package ru.practicum.shareit.user.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.WrongParameterException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findUserById(long id) {
        return UserMapper.toUserDto(userRepository.findUserById(id));
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUserCreate(userDto);
        return UserMapper.toUserDto(userRepository.create(user));
    }

    @Override
    public UserDto update(UserDto userDto, long userId) {
        findUserById(userId);
        if ((userDto.getName() == null) & (userDto.getEmail() == null)) {
            throw new WrongParameterException("Нет данных для обновления");
        }
        userDto.setId(userId);
        User user = UserMapper.toUserUpdate(userDto);
        return UserMapper.toUserDto(userRepository.update(user));
    }

    @Override
    public UserDto delete(long userId) {
        itemRepository.deleteItemsByIdUser(userId);
        return UserMapper.toUserDto(userRepository.delete(userId));
    }


}