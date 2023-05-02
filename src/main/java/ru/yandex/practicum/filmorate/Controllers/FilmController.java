package ru.yandex.practicum.filmorate.Controllers;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    Map<String, Film> films = new HashMap<>();


    /*
    при соблюдении всех условий добавляет фильм в мапу
     */
    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) {
        try {
            if (film.getName().isBlank() && film.getName().isEmpty()) {
                log.warn("Нет названия фильма");
                throw new ValidationException("Название не должно быть пустым");
            }
            if (film.getDescription().length() > 200) {
                log.warn("Слишком короткое описание фильма");
                throw new ValidationException("Описание должно быть менее 200 символов");
            }
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
                log.warn("Ошибка в дате релиза фильма");
                throw new ValidationException("Фильм не мог быть снять до 28 декабря 1895 года");
            }
            if (film.getDuration().isNegative() && film.getDuration().isZero()) {
                log.warn("у фильма отрицательная или нулевая продолжительность");
                throw new ValidationException("Продолжительность фильмы должна быть положительной");
            }
            films.put(film.getName(), film);
        } catch (ValidationException exception) {
            exception.getCause();
        }
        return film;
    }


    /*
    отдает список всех фильмов, что сейчас есть в хранилище
    */
    @GetMapping("/films")
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }


    /*
    обновляет фильм который уже лежал в мапе
     */
    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        try {
            if (film.getName().isBlank() && film.getName().isEmpty()) {
                throw new ValidationException("Название не должно быть пустым");
            }
            if (film.getDescription().length() > 200) {
                throw new ValidationException("Описание должно быть менее 200 символов");
            }
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
                throw new ValidationException("Фильм не мог быть снять до 28 декабря 1895 года");
            }
            if (film.getDuration().isNegative() && film.getDuration().isZero()) {
                throw new ValidationException("Продолжительность фильмы должна быть положительной");
            }
            films.put(film.getName(), film);
        } catch (ValidationException exception) {
            exception.getCause();
        }
        return film;
    }
}
