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

    private Set<Enum<Genre>> genres = new HashSet<>();

    private Enum<Rating> rating;

    public void setLikes(int userId) {
        likes.add(userId);

    }

    public void removeLike(int userId) {
        likes.remove(userId);
    }


    public void addGenre(String genre) {

        switch (genre) {
            case "Comedy" :
                genres.add(Genre.COMEDY);
                break;
            case "Drama" :
                genres.add(Genre.DRAMA);
                break;
            case "Cartoon" :
                genres.add(Genre.CARTOON);
                break;
            case "Thriller" :
                genres.add(Genre.THRILLER);
                break;
            case "Documentary" :
                genres.add(Genre.DOCUMENTARY);
                break;
            case "Action" :
                genres.add(Genre.ACTION);
                break;
        }
    }

    public void removeGenre(String genre) {
        switch (genre) {
            case "Comedy" :
                genres.remove(Genre.COMEDY);
                break;
            case "Drama" :
                genres.remove(Genre.DRAMA);
                break;
            case "Cartoon" :
                genres.remove(Genre.CARTOON);
                break;
            case "Thriller" :
                genres.remove(Genre.THRILLER);
                break;
            case "Documentary" :
                genres.remove(Genre.DOCUMENTARY);
                break;
            case "Action" :
                genres.remove(Genre.ACTION);
                break;
        }
    }
}
