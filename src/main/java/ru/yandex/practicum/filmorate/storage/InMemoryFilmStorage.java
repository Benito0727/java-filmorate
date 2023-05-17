package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 1;

    @Override
    public Film addFilm(Film film) {  // добавить фильм
        if (film.getId() < 1) {
            film.setId(filmId);
            filmId++;
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilm(int id) {  // получить фильм по ид
        if (!films.isEmpty()) {
            return films.get(id);
        } else {
            return null;
        }
    }

    @Override
    public Film removeFilm(Film film) {  // удалить фильм
        films.remove(film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {  // обновить существующий фильм
        try {
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
                log.info("Фильм с ID: {} успешно обновлен", film.getId());
            } else {
                log.warn("Попытка обновления несуществующего ID");
                throw new ValidationException("Несуществующий ID");
            }
            films.put(film.getId(), film);
        } catch (ValidationException exception) {
            exception.getCause();
        }
        return film;
    }

    @Override
    public List<Film> getFilmsList() {  // получить список фильмов
        return new ArrayList<>(films.values());
    }
}
