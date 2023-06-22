package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.mappers.databaseEntities.Genre;
import ru.yandex.practicum.filmorate.storage.dao.mappers.databaseEntities.Rating;

import java.util.*;

@Component("InMemoryFilmStorage")
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    private final TreeSet<Rating> ratings = new TreeSet<>(Set.of(
            new Rating(1, "G"),
            new Rating(2, "PG"),
            new Rating(3, "PG-13"),
            new Rating(4, "R"),
            new Rating(5, "NC-17")
    ));

    private final TreeSet<Genre> genres = new TreeSet<>(Set.of(
            new Genre(1, "COMEDY"),
            new Genre(2, "DRAMA"),
            new Genre(3, "CARTOON"),
            new Genre(4, "THRILLER"),
            new Genre(5, "DOCUMENTARY"),
            new Genre(6, "ACTION")
    ));

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
    public Optional<Film> getFilm(int id) {  // получить фильм по ид
        if (films.get(id) != null) {
            return Optional.of(films.get(id));
        } else {
            log.warn(String.format("Фильм с id %d не найден", id));
            throw new NotFoundException(String.format("фильм с id %d не найден", id));
        }
    }

    @Override
    public void removeFilm(int id) {  // удалить фильм
        if (!films.containsKey(id)) {
            throw new NotFoundException(String.format("Фильм с id %d не найден", id));
        }
        films.remove(id);
    }

    @Override
    public Optional<Film> updateFilm(@NotNull Film film) {  // обновить существующий фильм
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
        return Optional.of(film);
    }

    @Override
    public List<Film> getFilmsList() {  // получить список фильмов
        return new ArrayList<>(films.values());
    }

    public Set<Film> getMostPopularFilms() {
        mostPopularFilms.addAll(films.values());
        return mostPopularFilms.descendingSet();
    }

    @Override
    public Set<Genre> getGenres() {
        return new HashSet<>(genres);
    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        return Optional.of(new ArrayList<>(genres).get(id));
    }

    @Override
    public Set<Rating> getRatings() {
        return ratings.descendingSet();
    }

    @Override
    public Optional<Rating> getRatingById(int id) {
        return Optional.of(new ArrayList<>(ratings).get(id));
    }
}
