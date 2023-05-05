package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
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
