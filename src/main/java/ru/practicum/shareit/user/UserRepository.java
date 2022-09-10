package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    List<User> findAll();

    User findUserById(long id);

    User create(User user);

    User update(User user);

    User delete(long id);
}
