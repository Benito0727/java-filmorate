package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component("InMemoryUserStorage")
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    private int userId = 1;

    @Override
    public User addUser(@NotNull User user) {
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        if (user.getId() < 1) {
            user.setId(userId);
            userId++;
        }
        users.put(user.getId(), user);
        log.info("Пользователь с ID: {} успешно создан", user.getId());
        return user;
    }

    @Override
    public User removeUser(@NotNull User user) {
        if (users.get(user.getId()) == null) {
            log.warn(String.format("Пользователь с id %d не найден", user.getId()));
            throw new NotFoundException(String.format("Пользователь с id %d не найден", user.getId()));
        }
        users.remove(user.getId());
        return user;
    }

    @Override
    public Optional<User> updateUser(@NotNull User user) {
        try {
            if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
            if (users.containsKey(user.getId())) {
                log.info("Пользователь с ID: {} успешно обновлен", user.getId());
                users.put(user.getId(), user);
            } else {
                log.warn("Попытка обновления несуществующего ID");
                throw new NotFoundException("Несуществующий ID");
            }
        } catch (NotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
        return Optional.of(user);
    }

    @Override
    public List<User> getUserList() {
        if (!users.isEmpty()) {
            return new ArrayList<>(users.values());
        } else {
            log.warn("Запрос пустого списка пользователей");
            throw new NotFoundException("Список пользователей пуст");
        }
    }

    @Override
    public Optional<User> getUser(int id) {
        if (users.get(id) != null) {
            return Optional.of(users.get(id));
        } else {
            log.warn(String.format("Пользователь с id %d не найден", id));
            throw new NotFoundException(String.format("Пользователь с id %d не найден", id));
        }
    }
}
