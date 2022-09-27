package ru.practicum.shareit.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.WrongParameterException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<UserDto> findAll() {
        Collection<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto findUserById(long id) {
        User user = userRepository.findById(id).orElseThrow();
        return UserMapper.toUserDto(user);
    }

    @Transactional
    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUserCreate(userDto);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Transactional
    @Override
    public UserDto update(UserDto userDto, long userId) {
        if ((userDto.getName() == null) && (userDto.getEmail() == null)) {
            throw new WrongParameterException("Нет данных для обновления");
        }
        User userFromDb = userRepository.findById(userId).orElseThrow();
        User userUpdate = UserMapper.toUserUpdate(userDto);

        if (userUpdate.getName() != null) {
            userFromDb.setName(userUpdate.getName());
        }

        if (userUpdate.getEmail() != null) {
            userFromDb.setEmail(userUpdate.getEmail());
        }
        return UserMapper.toUserDto(userRepository.save(userFromDb));
    }

    @Transactional
    @Override
    public UserDto delete(long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        userRepository.delete(user);
        return UserMapper.toUserDto(user);
    }
}
