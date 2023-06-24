package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public  interface UserStorage {

    User addUser(User user);

    Optional<User> getUser(int id);

    User removeUser(User user);

    Optional<User> updateUser(User user);

    List<User> getUserList();
}
