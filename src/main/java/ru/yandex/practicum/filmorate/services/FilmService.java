package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;

@Service
public class FilmService {

    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
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
        filmStorage.getFilm(filmId).setLikes(userStorage.getUser(userId));
        return filmStorage.getFilm(filmId);
    }

    public Film removeLike(int userId, int filmId) {     // убрать лайк
        filmStorage.getFilm(filmId).removeLike(userStorage.getUser(userId));
        return filmStorage.getFilm(filmId);
    }

    public List<Film> getMostPopularFilm(int count) {       //получить список самых популярных фильмов
        return filmStorage.getMostPopularFilms(count);
    }
}
