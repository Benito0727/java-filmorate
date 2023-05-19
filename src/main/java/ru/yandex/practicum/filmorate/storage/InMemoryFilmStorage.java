package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    private final TreeSet<Film> mostPopularFilms;

    private int filmId = 1;


    public InMemoryFilmStorage() {
        mostPopularFilms = new TreeSet<>(Comparator.comparingInt(film -> film.getLikes().size()));
    }

    @Override
    public Film addFilm(@NotNull Film film) {  // добавить фильм
        if (film.getId() < 1) {
            film.setId(filmId);
            filmId++;
        }
        films.put(film.getId(), film);
        log.info(String.format("Фильм с id %d успешно добавлен", film.getId()));
        return film;
    }

    @Override
    public Film getFilm(int id) {  // получить фильм по ид
        if (films.get(id) != null) {
            return films.get(id);
        } else {
            log.warn(String.format("Фильм с id %d не найден", id));
            throw new NotFoundException(String.format("фильм с id %d не найден", id));
        }
    }

    @Override
    public Film removeFilm(@NotNull Film film) {  // удалить фильм
        if (!films.containsValue(film)) {
            throw new NotFoundException(String.format("Фильм с id %d не найден", film.getId()));
        }
        films.remove(film.getId());
        return film;
    }

    @Override
    public Film updateFilm(@NotNull Film film) {  // обновить существующий фильм
        try {
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
                log.info("Фильм с ID: {} успешно обновлен", film.getId());
            } else {
                log.warn(String.format("Попытка обновления несуществующего id - %d", film.getId()));
                throw new NotFoundException(String.format("Фильма с таким id: %d не существует", film.getId()));
            }
            films.put(film.getId(), film);
        } catch (NotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
        return film;
    }

    @Override
    public List<Film> getFilmsList() {  // получить список фильмов
        return new ArrayList<>(films.values());
    }

    public Set<Film> getMostPopularFilms() {
        mostPopularFilms.addAll(films.values());
        NavigableSet<Film> filmByCount = mostPopularFilms.descendingSet();
        return filmByCount;
    }
}
