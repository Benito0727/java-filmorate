package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
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
        userStorage.getUser(id).addFriends(userStorage.getUser(friendId));
        return userStorage.getUser(friendId);
    }

    public User removeFriend(int id, int friendId) {
        userStorage.getUser(id).removeFriends(userStorage.getUser(friendId));
        return userStorage.getUser(friendId);
    }

    public List<User> getMutualFriends(User user, User otherUser) {
        ArrayList<User> mutualFriends = new ArrayList<>();
        for (User friend : user.getFriends()) {
            if (otherUser.getFriends().contains(friend)) {
                mutualFriends.add(friend);
            }
        }
        return mutualFriends;
    }

    public List<User> getFriendList(User user) {
        return new ArrayList<>(user.getFriends());
    }
}
