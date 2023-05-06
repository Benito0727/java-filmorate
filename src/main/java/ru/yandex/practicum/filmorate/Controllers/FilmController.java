package ru.yandex.practicum.filmorate.Controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
public class FilmController {

    Map<Integer, Film> films = new HashMap<>();
    private int filmId = 1;

    /*
    при соблюдении всех условий добавляет фильм в мапу
     */

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody @NotNull Film film) {
        if (isValid(film)) {
            if (film.getId() < 1) {
                film.setId(filmId);
                filmId++;
            }
            films.put(film.getId(), film);
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

    @PutMapping(value = "/films", produces = APPLICATION_JSON_VALUE)
    public Film updateFilm(@Valid @RequestBody @NotNull Film film) {
        try {
            if (isValid(film)) {
                if (films.containsKey(film.getId())) {
                    films.put(film.getId(), film);
                    log.info("Фильм с ID: {} успешно обновлен", film.getId());
                } else {
                    log.warn("Попытка обновления несуществующего ID");
                    throw new ValidationException("Несуществующий ID");
                }
            }
        } catch (ValidationException exception) {
            throw new RuntimeException(exception.getMessage());
        }
        return film;
    }

    private boolean isValid(Film film) {
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

    private boolean isIncorrectName(String name) {
        return name.isBlank() || name.isEmpty();
    }
}
