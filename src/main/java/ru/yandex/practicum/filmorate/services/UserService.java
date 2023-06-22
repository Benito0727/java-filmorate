package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDBStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User getUser(int id) {
        if (userStorage.getUser(id).isPresent()) {
            return userStorage.getUser(id).get();
        } else {
            throw new NotFoundException(String.format("Пользователь с ID %d не найден", id));
        }
    }

    public User updateUser(User user) {
        if (userStorage.updateUser(user).isPresent()) {
            return userStorage.updateUser(user).get();
        } else {
            throw new NotFoundException(String.format("Пользователь с ID %d не найден", user.getId()));
        }
    }

    public List<User> getUserList() {
        return userStorage.getUserList();
    }

    public User addFriend(int id, int friendId) {
        if (userStorage.getUser(id).isPresent()) {
            if (userStorage.getUser(friendId).isPresent()) {
                User user = userStorage.getUser(id).get();
                User friend = userStorage.getUser(friendId).get();
                user.addFriends(friendId);

                if (friend.getFriends().containsKey(id)) {
                    user.updateFriend(friendId, true);
                    friend.updateFriend(id, true);
                }

                userStorage.updateUser(user);
                userStorage.updateUser(friend);

                return userStorage.getUser(id).get();
            } else {
                throw new NotFoundException(String.format("Не нашли друга с ID %d", friendId));
            }
        } else {
            throw new NotFoundException(String.format("Не нашли пользователя с ID %d", id));
        }
    }

    public User removeFriend(int id, int friendId) {
        try {
            if (isUserInStorage(id) && isUserInStorage(friendId)) {
                log.info("Пользователь " + friendId + " удален из друзей пользователя " + id);
                Optional<User> user = userStorage.getUser(id);
                if (user.isPresent()) {
                    user.get().removeFriends(friendId);
                    Optional<User> friend = userStorage.getUser(friendId);
                    if (friend.isPresent()) {
                        friend.get().removeFriends(id);
                        userStorage.updateUser(user.get());
                        userStorage.updateUser(friend.get());
                        Optional<User> returningUser = userStorage.getUser(id);
                        if (returningUser.isPresent()) {
                            return returningUser.get();
                        }
                    }
                }
            }
            throw new RuntimeException("Ошибка удаления из друзей");
        } catch (NotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    // todo
    public List<User> getMutualFriends(int userId, int otherUserId) {
        Optional<User> user = userStorage.getUser(userId);
        Optional<User> otherUser = userStorage.getUser(otherUserId);
        if (user.isPresent() && otherUser.isPresent()) {
            List<Optional<User>> optUsers = user.get().getFriends().keySet().stream()
                    .filter(otherUser.get().getFriends().keySet()::contains)
                    .map(userStorage::getUser).collect(Collectors.toList());
            ArrayList<User> users = new ArrayList<>();
            for (Optional<User> optionalUser : optUsers) {
                if (optionalUser.isPresent()) {
                    users.add(optionalUser.get());
                } else {
                    throw new NotFoundException();
                }
            }
            return users;
        } else {
            throw new NotFoundException();
        }
    }

    //todo
    public List<User> getFriendList(int id) {
        if (userStorage.getUser(id).isPresent()) {
            User user = userStorage.getUser(id).get();
            List<Optional<User>> optUsers = user.getFriends().keySet().stream()
                    .map(userStorage::getUser).collect(Collectors.toList());
            ArrayList<User> users = new ArrayList<>();
            for (Optional<User> optionalUser : optUsers) {
                if (optionalUser.isPresent()) {
                    users.add(optionalUser.get());
                } else {
                    throw new NotFoundException();
                }
            }
            return users.stream().sorted(Comparator.comparing(User::getId)).collect(Collectors.toList());
        } else {
            throw new NotFoundException();
        }
    }

    private boolean isUserInStorage(int id) {
        if (userStorage.getUser(id).isEmpty()) {
            log.warn(String.format("Ошибка: пользователь с %d не найден", id));
            throw new NotFoundException(String.format("Пользователь с id %d не найден", id));
        }
        return true;
    }
}
