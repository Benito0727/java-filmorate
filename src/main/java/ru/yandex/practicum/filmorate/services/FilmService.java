package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final InMemoryFilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) {   // добавить фильм
        return filmStorage.addFilm(film);
    }

    public Film removeFilm(Film film) {  // удалить фильм
        return filmStorage.removeFilm(film);
    }

    public Film updateFilm(Film film) {  // обновить фильм
        return filmStorage.updateFilm(film);
    }

    public Film getFilm(int id) {  // получить фильм по id
        return filmStorage.getFilm(id);
    }

    public List<Film> getFilmsList() {
        return filmStorage.getFilmsList();
    }

    public Film addLike(int userId, int filmId) {  // поставить лайк
        Film film = filmStorage.getFilm(filmId);
        film.setLikes(userId);
        return film;
    }

    public Film removeLike(int userId, int filmId) {     // убрать лайк
        Film film = filmStorage.getFilm(filmId);
        if (!film.getLikes().contains(userId)) {
            throw new NotFoundException(String.format("Пользователь с id %d" +
                    " не ставил лайки под этим фильмом", userId));
        }
        film.removeLike(userId);
        return film;
    }

    public Set<Film> getMostPopularFilm(int count) {       //получить список самых популярных фильмов
        return filmStorage.getMostPopularFilms().stream().limit(count).collect(Collectors.toSet());
    }
}
