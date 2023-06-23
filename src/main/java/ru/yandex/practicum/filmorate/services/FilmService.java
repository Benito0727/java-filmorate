package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dao.mappers.databaseEntities.Genre;
import ru.yandex.practicum.filmorate.storage.dao.mappers.databaseEntities.Rating;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDBStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) {   // добавить фильм
        return filmStorage.addFilm(film);
    }

    public void removeFilm(int id) {  // удалить фильм
        filmStorage.removeFilm(id);
    }

    public Film updateFilm(Film film) {  // обновить фильм
        if (filmStorage.getFilm(film.getId()).isPresent()) {
            filmStorage.updateFilm(film);
            return filmStorage.getFilm(film.getId()).get();
        } else {
            throw new NotFoundException(String.format("Фильм с ID %d не найден", film.getId()));
        }
    }

    public Film getFilm(int id) {  // получить фильм по id
        if (filmStorage.getFilm(id).isPresent()) {
            return filmStorage.getFilm(id).get();
        } else {
            throw new NotFoundException(String.format("Фильм с ID %d не найден", id));
        }
    }

    public List<Film> getFilmsList() {  // получить список всех фильмов
        return filmStorage.getFilmsList();
    }

    public Film addLike(int userId, int filmId) {  // поставить лайк
        if (filmStorage.getFilm(filmId).isPresent()) {
            Film film = filmStorage.getFilm(filmId).get();
            film.setLikes(userId);
            filmStorage.updateFilm(film);
            return film;
        } else {
            throw new NotFoundException(String.format("Фильм с ID %d не найден", filmId));
        }
    }

    public Set<Genre> getFilmGenres() {
        return filmStorage.getGenres();
    }

    public Genre getFilmGenreById(int id) {
        Optional<Genre> genre = filmStorage.getGenreById(id);
        if (genre.isPresent()) {
            log.info(String.format("Вернули жанр с ID %d", id));
            return genre.get();
        } else {
            log.warn(String.format("Не нашли жанр с ID %d", id));
            throw new NotFoundException(String.format("Нет жанра с ID %d", id));
        }
    }

    public Set<Rating> getFilmsRatings() {
        return filmStorage.getRatings();
    }

    public Rating getRatingById(int id) {
        Optional<Rating> rating = filmStorage.getRatingById(id);
        if (rating.isPresent()) {
           log.info(String.format("Вернули рейтингом с ID %d", id));
           return rating.get();
        } else {
           log.warn(String.format("Не нашли рейтинг с ID %d", id));
           throw new NotFoundException(String.format("Нет рейтинга с ID %d", id));
        }
    }

    public void removeLike(int userId, int filmId) {     // убрать лайк
        if (filmStorage.getFilm(filmId).isPresent()) {
            Film film = filmStorage.getFilm(filmId).get();
            if (!film.getLikes().contains(userId)) {
                throw new NotFoundException(String.format("Пользователь с id %d" +
                        " не ставил лайки под этим фильмом", userId));
            }
            film.removeLike(userId);
            filmStorage.updateFilm(film);
        } else {
            throw new NotFoundException(String.format("Фильм с ID %d не найден", filmId));
        }
    }

    public Set<Film> getMostPopularFilm(int count) {       //получить список самых популярных фильмов
        return filmStorage.getMostPopularFilms().stream().limit(count).collect(Collectors.toSet());
    }
}
