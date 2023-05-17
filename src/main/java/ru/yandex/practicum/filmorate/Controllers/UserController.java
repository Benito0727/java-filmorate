package ru.yandex.practicum.filmorate.Controllers;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*
    отдает список пользователей
     */

    @GetMapping("/users")
    public List<User> getUsersList() {
        return userService.getUserList();
    }

    /*
    при соблюдении условий
    добавляет нового пользователя
     */

    @PostMapping("/users")
    public User addUser(@Valid @RequestBody @NotNull User user) {
        if (isValid(user)) {
            return userService.addUser(user);
        }
        return user;
    }

    /*
    при соблюдении условий
    обновляет уже существующего пользователя
     */

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody @NotNull User user) {
        if (isValid(user)) {
            return userService.updateUser(user);
        }
        return user;
    }

    private boolean isValid(@org.jetbrains.annotations.NotNull User user) {
        boolean valid;
        try {
            if (isIncorrectEmail(user.getEmail())) {
                log.warn("Некорректный email: {}", user.getEmail());
                throw new ValidationException("Некорректный email");
            }
            if (isIncorrectLogin(user.getLogin())) {
                log.warn("Некорректный login: {}", user.getLogin());
                throw new ValidationException("Некорректный login");
            }
            if (user.getBirthday().isAfter(LocalDate.now())) {
                log.warn("Некорректная дата рождения: {}", user.getBirthday());
                throw new ValidationException("День рождения не может быть в будущем");
            }
            valid = true;
        } catch (ValidationException exception) {
            throw new RuntimeException(exception.getMessage());
        }
        return valid;
    }

    private boolean isIncorrectLogin(@NotNull String login) {
        return login.isEmpty() || login.contains(" ");
    }

    private boolean isIncorrectEmail(@NotNull String email) {
        return !email.contains("@");
    }
}
