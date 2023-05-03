package ru.yandex.practicum.filmorate.model;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

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
}
