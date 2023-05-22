package ru.yandex.practicum.filmorate.model;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.*;

@Data
public class User {

    private int id;                 // ID пользователя

    @Email
    private String email;           //  email пользователя

    @NotEmpty
    @NotBlank
    private String login;           // login пользователя

    private String name;            // имя пользователя используемое для отображения

    @PastOrPresent
    private LocalDate birthday;     // день рождения пользователя

    private Set<Integer> friends = new HashSet<>();

    public void removeFriends(int id) {
        friends.remove(id);
    }

    public void addFriends(int id) {
        friends.add(id);
    }

}
