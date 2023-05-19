package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private int id;                     // ID фильмы

    @NotEmpty()
    private String name;                // название фильма

    private String description;         // описание фильма

    private LocalDate releaseDate;      // дата релиза фильма

    @Positive
    private int duration;          // продолжительность фильма

    private Set<Integer> likes = new HashSet<>(); // лайки фильма

    public void setLikes(int userId) {
        likes.add(userId);

    }

    public void removeLike(int userId) {
        likes.remove(userId);
    }
}
