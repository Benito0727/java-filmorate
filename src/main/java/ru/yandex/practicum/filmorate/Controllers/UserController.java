package ru.yandex.practicum.filmorate.Controllers;

import jakarta.validation.ValidationException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    Map<String, User> users = new HashMap<>();
    private int userId = 0;
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
    public User addUser(@RequestBody User user) {
        try {
            if (user.getId() < 1) {
                user.setId(userId);
                userId++;
            }
            if (user.getEmail() == null && !user.getEmail().contains("@")) {
                throw new ValidationException("Некорректный email");
            }
            if (user.getLogin() == null && user.getLogin().contains(" ")) {
                throw new ValidationException("Некорректный login");
            }
            if (user.getName().isEmpty() && user.getName() == null) user.setName(user.getLogin());
            if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("День рождения не может быть в будущем");
            }
            users.put(user.getEmail(), user);
        } catch (ValidationException exception) {
            exception.getCause();
        }
        return user;
    }

    /*
    при соблюдении условий
    обновляет уже существующего пользователя
     */
    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        try {
            if (user.getEmail().isEmpty() && !user.getEmail().contains("@")) {
                throw new ValidationException("Некорректный email");
            }
            if (user.getLogin().isEmpty() && user.getLogin().contains(" ")) {
                throw new ValidationException("Некорректный login");
            }
            if (user.getName().isEmpty() && user.getName() == null) user.setName(user.getLogin());
            if (user.getBirthday().isAfter(LocalDate.now())) {
                throw new ValidationException("День рождения не может быть в будущем");
            }
            users.put(user.getEmail(), user);
        } catch (ValidationException exception) {
            exception.getCause();
        }
        return user;
    }

}
