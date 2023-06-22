package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.mappers.databaseEntities.Genre;
import ru.yandex.practicum.filmorate.storage.dao.mappers.databaseEntities.Rating;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FilmStorage {

    Film addFilm(Film film);

    Optional<Film> getFilm(int id);

    void removeFilm(int id);

    Optional<Film> updateFilm(Film film);

    List<Film> getFilmsList();

    Set<Film> getMostPopularFilms();

    Set<Genre> getGenres();

    Optional<Genre> getGenreById(int id);

    Set<Rating> getRatings();

    Optional<Rating> getRatingById(int id);
}
