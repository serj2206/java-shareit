package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserRepository {

    Collection<User> findAll();

    User findUserById(long id);

    User create(User user);

    User update(User user);

    User delete(long id);
}
