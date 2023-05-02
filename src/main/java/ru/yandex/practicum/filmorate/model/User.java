package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private int id;                 // ID пользователя
    private String email;           //  email пользователя
    private String login;           // login пользователя
    private String name;            // имя пользователя используемое для отображения
    private LocalDate birthday;     // день рождения пользователя
}
