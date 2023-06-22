package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;
import org.jetbrains.annotations.NotNull;
import ru.yandex.practicum.filmorate.storage.dao.mappers.databaseEntities.Genre;
import ru.yandex.practicum.filmorate.storage.dao.mappers.databaseEntities.Rating;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    /*
    добавляет фильм в хранилище
     */

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody @NotNull Film film) {
        isValid(film);
        return filmService.addFilm(film);
    }

    /*
    отдает список всех фильмов, что сейчас есть в хранилище
    */

    @GetMapping("/films")
    public List<Film> getFilms() {
        try {
            return filmService.getFilmsList();
        } catch (NotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    /*
    отдает фильм по id
     */

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable int id) {
        return filmService.getFilm(id);
    }

    /*
    обновляет фильм который уже лежал в хранилище
     */

    @PutMapping(value = "/films", produces = APPLICATION_JSON_VALUE)
    public Film updateFilm(@Valid @RequestBody @NotNull Film film) {
        isValid(film);
        return filmService.updateFilm(film);
    }

    /*
        пользователь userId ставит лайк фильму filmId
     */

    @PutMapping("/films/{id}/like/{userId}")
    public Film setLike(@PathVariable(value = "id") int filmId,
                        @PathVariable(value = "userId") int userId) {
        return filmService.addLike(userId, filmId);
    }

    /*
    пользователь userId убирает лайк у фильма filmId
     */

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLike(@PathVariable(value = "id") int filmId,
                           @PathVariable(value = "userId") int userId) {
        filmService.removeLike(userId, filmId);
    }

    @DeleteMapping("/films/{id}")
    public void deleteFilm(@PathVariable(value = "id") int id) {
        filmService.removeFilm(id);
    }

    /*
    получить самые залайканые фильмы в количестве count, либо первая десятка
     */
    @GetMapping("/films/popular")
    public Set<Film> getMostPopularFilm(@RequestParam(defaultValue = "10") int count) {
        return filmService.getMostPopularFilm(count);
    }

    // todo
    @GetMapping("/genres")
    public Set<Genre> getFilmGenres() {
        return filmService.getFilmGenres();
    }

    //todo
    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable int id) {
        return filmService.getFilmGenreById(id);
    }

    @GetMapping("/mpa")
    public Set<Rating> getMPA() {
        return filmService.getFilmsRatings();
    }

    @GetMapping("/mpa/{id}")
    public Rating getMPAById(@PathVariable int id) {
        return filmService.getRatingById(id);
    }

    private void isValid(@NotNull Film film) {
        int descLength = film.getDescription().length();
        try {
            if (isIncorrectName(film.getName())) {
                log.warn("Нет названия фильма");
                throw new ValidationException("Название не должно быть пустым");
            }
            if (descLength > 200) {
                log.warn("Слишком длинное описание фильма - {} символов", film.getDescription().length());
                throw new ValidationException("Описание должно быть менее 200 символов");
            }
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                log.warn("Ошибка в дате релиза фильма: {}", film.getReleaseDate());
                throw new ValidationException("Фильм не мог быть снять до 28 декабря 1895 года");
            }
            if (film.getDuration() <= 0) {
                log.warn("у фильма отрицательная или нулевая продолжительность: {}", film.getDuration());
                throw new ValidationException("Продолжительность фильмы должна быть положительной");
            }

        } catch (ValidationException exception) {
            throw new ValidationException(exception.getMessage());
        }
    }



    private boolean isIncorrectName(@NotNull String name) {
        return name.isBlank() || name.isEmpty();
    }
}
