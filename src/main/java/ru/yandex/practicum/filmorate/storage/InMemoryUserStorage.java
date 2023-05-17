package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    private int userId = 1;

    @Override
    public User addUser(User user) {
        setUserName(user);
        if (user.getId() < 1) {
            user.setId(userId);
            userId++;
        }
        users.put(user.getId(), user);
        log.info("Пользователь с ID: {} успешно создан", user.getId());
        return user;
    }

    @Override
    public User removeUser(User user) {
        users.remove(user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        try {
            setUserName(user);
            if (users.containsKey(user.getId())) {
                log.info("Пользователь с ID: {} успешно обновлен", user.getId());
                users.put(user.getId(), user);
            } else {
                log.warn("Попытка обновления несуществующего ID");
                throw new ValidationException("Несуществующий ID");
            }
        } catch (ValidationException exception) {
            throw new RuntimeException(exception.getMessage());
        }
        return user;
    }

    @Override
    public List<User> getUserList() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(int id) {
        return users.get(id);
    }

    private void setUserName(User user) {
        if (user.getName() == null) user.setName(user.getLogin());
    }
}
