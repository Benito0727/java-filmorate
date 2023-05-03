package ru.yandex.practicum.filmorate.Controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {

    Map<Integer, User> users = new HashMap<>();
    private int userId = 1;
    /*
    отдает список пользователей
     */
    @GetMapping("/users")
    public List<User> getUsersList(){
        return new ArrayList<>(users.values());
    }


    /*
    при соблюдении условий
    добавляет нового пользователя
     */
    @PostMapping("/users")
    public User addUser(@Valid @RequestBody @NotNull User user) {
        try {
            if (user.getEmail() == null || !user.getEmail().contains("@")) {
                log.warn("Некорректный email: {}", user.getEmail());
                throw new ValidationException("Некорректный email");
            }
            if (user.getLogin() == null || user.getLogin().contains(" ")) {
                log.warn("Некорректный login: {}", user.getLogin());
                throw new ValidationException("Некорректный login");
            }
            if (user.getName() == null) user.setName(user.getLogin());
            if (user.getBirthday().isAfter(LocalDate.now())) {
                log.warn("Некорректная дата рождения: {}", user.getBirthday());
                throw new ValidationException("День рождения не может быть в будущем");
            }
            if (user.getId() < 1) {
                user.setId(userId);
                userId++;

            }
            users.put(user.getId(), user);
            log.info("Пользователь с ID: {} успешно создан", user.getId());
        } catch (ValidationException exception) {
            throw new RuntimeException(exception.getMessage());
        }
        return user;
    }

    /*
    при соблюдении условий
    обновляет уже существующего пользователя
     */
    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody @NotNull User user) {
        try {
            if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
                log.warn("Некорректный email: {}", user.getEmail());
                throw new jakarta.validation.ValidationException("Некорректный email");
            }
            if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
                log.warn("Некорректный login: {}", user.getLogin());
                throw new ValidationException("Некорректный login");
            }
            if (user.getName() == null) user.setName(user.getLogin());
            if (user.getBirthday().isAfter(LocalDate.now())) {
                log.warn("Некорректная дата рождения: {}", user.getBirthday());
                throw new ValidationException("День рождения не может быть в будущем");
            }
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
}
