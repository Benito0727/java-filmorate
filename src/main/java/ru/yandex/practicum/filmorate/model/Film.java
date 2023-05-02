package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    private int id;                     // ID фильмы
    private String name;                // название фильма
    private String description;         // описание фильма
    private LocalDate releaseDate;      // дата релиза фильма
    private Duration duration;          // продолжительность фильма
}
