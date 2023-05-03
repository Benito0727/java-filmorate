package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {

    private int id;                     // ID фильмы

    @NotEmpty()
    private String name;                // название фильма

    private String description;         // описание фильма

    private LocalDate releaseDate;      // дата релиза фильма

    @Positive
    private int duration;          // продолжительность фильма

}
