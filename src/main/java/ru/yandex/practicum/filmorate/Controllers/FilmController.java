package ru.yandex.practicum.filmorate.Controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;
import org.jetbrains.annotations.NotNull;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

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
        if (isValid(film)) {
            filmService.addFilm(film);
        }
        return film;
    }

    /*
    отдает список всех фильмов, что сейчас есть в хранилище
    */

    @GetMapping("/films")
    public List<Film> getFilms() {
        try {
            return filmService.getFilmsList();
        } catch (NullPointerException exception) {
            throw new RuntimeException();
        }
    }

    /*
    обновляет фильм который уже лежал в хранилище
     */

    @PutMapping(value = "/films", produces = APPLICATION_JSON_VALUE)
    public Film updateFilm(@Valid @RequestBody @NotNull Film film) {
        if (isValid(film)) filmService.updateFilm(film);
        return film;
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film setLike(@PathVariable(value = "id") int filmId,
                        @PathVariable(value = "userId") int userId) {
        return filmService.addLike(userId, filmId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film removeLike(@PathVariable(value = "id") int filmId,
                           @PathVariable(value = "userId") int userId) {
        return filmService.removeLike(userId, filmId);
    }

    @GetMapping("/films/popular")
    public List<Film> getMostPopularFilm(@RequestParam(defaultValue = "10") int count) {
        return filmService.getMostPopularFilm(count);
    }

    private boolean isValid(@NotNull Film film) {
        boolean valid;
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
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
                log.warn("Ошибка в дате релиза фильма: {}", film.getReleaseDate());
                throw new ValidationException("Фильм не мог быть снять до 28 декабря 1895 года");
            }
            if (film.getDuration() <= 0) {
                log.warn("у фильма отрицательная или нулевая продолжительность: {}", film.getDuration());
                throw new ValidationException("Продолжительность фильмы должна быть положительной");
            }
            valid = true;
        } catch (ValidationException exception) {
            throw new RuntimeException(exception.getMessage());
        }
        return valid;
    }

    private boolean isIncorrectName(@NotNull String name) {
        return name.isBlank() || name.isEmpty();
    }
}
