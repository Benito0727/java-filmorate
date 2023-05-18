package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;

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
        try {
            if (userStorage.getUser(id) != null) {
                return userStorage.getUser(id);
            } else {
                throw new NotFoundException("По запросу ничего не найдено");
            }
        } catch (NotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
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
                userStorage.getUser(id).addFriends(userStorage.getUser(friendId));
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
                User user = userStorage.getUser(id);
                User friend = userStorage.getUser(friendId);
                if (user.getFriends().contains(friend)) {
                    log.info("Пользователь " + friendId + " удален из друзей пользователя " + id);
                    user.removeFriends(friend);
                    return userStorage.getUser(id);
                } else {
                    throw new NotFoundException("Пользователя с id " + friendId +
                            " нет в списке друзей пользователя с id " + id);
                }
            }
            throw new RuntimeException("Ошибка удаления из друзей");
        } catch (ValidationException exception) {
            throw new ValidationException(exception.getMessage());
        } catch (NotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    public List<User> getMutualFriends(@NotNull User user, User otherUser) {
        ArrayList<User> mutualFriends = new ArrayList<>();
        for (User friend : user.getFriends()) {
            if (otherUser.getFriends().contains(friend)) {
                mutualFriends.add(friend);
            }
        }
        return mutualFriends;
    }

    public List<User> getFriendList(@NotNull User user) {
        return new ArrayList<>(user.getFriends());
    }

    private boolean isUserInStorage(int id) {
        if (userStorage.getUser(id) == null) {
            log.warn(String.format("Ошибка: пользователь с %d не найден", id));
            throw new NotFoundException(String.format("Пользователь с id %d не найден", id));
        }
        return true;
    }
}
