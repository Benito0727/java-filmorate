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
    отдает пользователя по id
     */
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUser(id);
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

    /*
    Добавить пользователю с id в друзья пользователя с friendId
     */
    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable(value = "id") int id,
                          @PathVariable(value = "friendId")  int friendId) {
        userService.addFriend(id, friendId);
        return userService.getUser(friendId);
    }

    /*
    Удалить из друзей пользователя id, пользователя friendId
     */
    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable(value = "id") int id,
                             @PathVariable(value = "friendId") int friendId) {
        userService.removeFriend(id, friendId);
        return userService.getUser(id);
    }

    /*
    получить список друзей пользователя
     */
    @GetMapping("/users/{id}/friends")
    public List<User> getFriendsList(@PathVariable int id) {
        return userService.getFriendList(id);
    }

    /*
    получить список общих друзей пользователя id с friendId
     */
    @GetMapping("/users/{id}/friends/common/{friendId}")
    public List<User> getMutualFriends(@PathVariable(value = "id") int id,
                                       @PathVariable(value = "friendId") int friendId) {
        return userService.getMutualFriends(id, friendId);
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
