package ru.practicum.shareit.user.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.exception.WrongParameterException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Component

public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 0;


    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User findUserById(long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else throw new WrongParameterException(String.format("Пользователя с id = %d нет в базе", id));
    }

    @Override
    public User create(User user) {
        if (users.values().stream()
                .filter(user1 -> user1.getEmail().equals(user.getEmail()))
                .count() != 0) {
            throw new ValidationException("Пользователь с указанным e-mail уже есть.");
        } else {
            user.setId(getId());
            users.put(user.getId(), user);
        }
        return user;
    }

    @Override
    public User update(User user) {

        //Проверка на оригинальность email
        if ((users.values().stream()
                .filter(user1 -> user1.getEmail().equals(user.getEmail()))
                .filter(user1 -> user1.getId() != user.getId()).count()) != 0) {
            throw new ValidationException("Указанный e-mail уже зарегистрирован другим пользователем.");
        }

        //Если имя пустое, то взять имя из БД
        if (user.getName() == null) {
            user.setName(users.get(user.getId()).getName());
        }

        //Если email пустой, то взять email из БД
        if (user.getEmail() == null) {
            user.setEmail(users.get(user.getId()).getEmail());
        }

        //Поместить обновленный user в БД
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public User delete(long id) {
        if (users.containsKey(id)) {
            return users.remove(id);
        }
        throw new WrongParameterException(String.format("Пользователя с id %d в базе нет", id));
    }

    private long getId() {
        return ++id;
    }


}
