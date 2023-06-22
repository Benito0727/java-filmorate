package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.yandex.practicum.filmorate.storage.dao.mappers.databaseEntities.Genre;
import ru.yandex.practicum.filmorate.storage.dao.mappers.databaseEntities.Mpa;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.*;

@Data
public class Film {

    private int id;                                             // ID фильмы

    @NotEmpty()
    private String name;                                        // название фильма

    private String description;                                 // описание фильма

    private LocalDate releaseDate;                              // дата релиза фильма

    @Positive
    private int duration;                                       // продолжительность фильма

    private Set<Integer> likes = new HashSet<>();               // лайки фильма

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private Set<Genre> genres = new HashSet<>();      // жанры фильма

    private Mpa mpa;         // рейтинг фильма

    public Film() {

    }

    public Film(int id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public void setLikes(int userId) {
        likes.add(userId);

    }

    public void removeLike(int userId) {
        likes.remove(userId);
    }
}
