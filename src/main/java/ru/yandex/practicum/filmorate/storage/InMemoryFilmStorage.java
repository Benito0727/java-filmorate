package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    private final Set<Film> mostPopularFilms;

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
    public Film removeFilm(@NotNull Film film) {  // удалить фильм
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

    public List<Film> getMostPopularFilms(int count) {
        mostPopularFilms.addAll(films.values());
        ArrayList<Film> mostPopularByCount = new ArrayList<>();
        mostPopularFilms.stream().limit(count).forEach(mostPopularByCount::add);
        return mostPopularByCount;
    }
}
