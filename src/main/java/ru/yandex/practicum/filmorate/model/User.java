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

    private HashMap<Integer, Boolean> friends = new HashMap<>();  // список друзей пользователя (true - дружба, false - подписка)

    public void removeFriends(int id) {
        friends.remove(id);
    }

    public void addFriends(int id) {
        friends.put(id, false);
    }

    public User() {

    }

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public void updateFriend(int id, boolean friendship) {
        friends.put(id, friendship);
    }

    public LocalDate getBirthday() {
        return this.birthday;
    }
}
