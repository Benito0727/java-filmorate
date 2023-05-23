package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final InMemoryUserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User getUser(int id) {
        return userStorage.getUser(id);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> getUserList() {
        return userStorage.getUserList();
    }

    public User addFriend(int id, int friendId) {
        try {
            if (isUserInStorage(id) && isUserInStorage(friendId)) {
                userStorage.getUser(id).addFriends(friendId);
                userStorage.getUser(friendId).addFriends(id);
            }
            return userStorage.getUser(id);
        } catch (ValidationException exception) {
            throw new ValidationException(exception.getMessage());
        } catch (NotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    public User removeFriend(int id, int friendId) {
        try {
            if (isUserInStorage(id) && isUserInStorage(friendId)) {
                log.info("Пользователь " + friendId + " удален из друзей пользователя " + id);
                userStorage.getUser(id).removeFriends(friendId);
                userStorage.getUser(friendId).removeFriends(id);
                return userStorage.getUser(id);
            }
            throw new RuntimeException("Ошибка удаления из друзей");
        } catch (NotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    public List<User> getMutualFriends(int userId, int otherUserId) {
        return userStorage.getUser(userId).getFriends().stream().
                filter(userStorage.getUser(otherUserId).getFriends()::contains).
                map(userStorage::getUser).collect(Collectors.toList());
    }

    public List<User> getFriendList(int id) {
        return userStorage.getUser(id).getFriends().stream().
                map(userStorage::getUser).
                sorted(Comparator.comparing(User::getId)).
                collect(Collectors.toList());
    }

    private boolean isUserInStorage(int id) {
        if (userStorage.getUser(id) == null) {
            log.warn(String.format("Ошибка: пользователь с %d не найден", id));
            throw new NotFoundException(String.format("Пользователь с id %d не найден", id));
        }
        return true;
    }
}
