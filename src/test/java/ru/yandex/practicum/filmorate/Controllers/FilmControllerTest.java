package ru.yandex.practicum.filmorate.Controllers;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class FilmControllerTest {

    private FilmController controller;


    private FilmService filmService;
    private static final TestUnits unit = new TestUnits();

    @BeforeEach
    public void setup() {
        filmService = new FilmService(new InMemoryFilmStorage());
        controller = new FilmController(filmService);
    }

    @Test
    public void shouldAddFilm() {
        Film film = unit.getFilm();
        controller.addFilm(film);

        assertEquals(film, filmService.getFilm(1));
    }

    @Test
    public void shouldGetExceptionIfFilmHaveIncorrectField() {
        assertThrows(RuntimeException.class, () -> controller.addFilm(unit.getFilmWithIncorrectName()));
        assertThrows(RuntimeException.class, () -> controller.addFilm(unit.getFilmWithIncorrectDescription()));
        assertThrows(RuntimeException.class, () -> controller.addFilm(unit.getFilmWithIncorrectReleaseDate()));
        assertThrows(RuntimeException.class, () -> controller.addFilm(unit.getFilmWithIncorrectDuration()));

        assertThrows(RuntimeException.class, () -> controller.updateFilm(unit.getFilmWithIncorrectName()));
        assertThrows(RuntimeException.class, () -> controller.updateFilm(unit.getFilmWithIncorrectDescription()));
        assertThrows(RuntimeException.class, () -> controller.updateFilm(unit.getFilmWithIncorrectReleaseDate()));
        assertThrows(RuntimeException.class, () -> controller.updateFilm(unit.getFilmWithIncorrectDuration()));
        assertThrows(RuntimeException.class, () -> controller.updateFilm(unit.getFilm()));
    }

    @Test
    public void shouldGetFilms() {
        Film film = unit.getFilm();
        ArrayList<Film> films = new ArrayList<>();

        films.add(film);
        controller.addFilm(film);

        assertEquals(films.get(0), controller.getFilms().get(0));
        assertEquals(List.of(film), controller.getFilms());
    }

    @Test
    public void shouldUpdateFilm() {
        Film film = unit.getFilm();
        film.setId(1);
        controller.addFilm(unit.getFilm());

        controller.updateFilm(film);

        assertEquals(film, controller.getFilms().get(0));
    }

    @Test
    public void shouldGetFilmById() {
        Film film1 = unit.getFilm();

        controller.addFilm(film1);

        assertEquals(film1, controller.getFilmById(1));
        assertThrows(NotFoundException.class, () -> controller.getFilmById(999));
    }


    @Test
    public void shouldAddLike() {
        Film film1 = unit.getFilm();

        controller.addFilm(film1);
        controller.setLike(1, 1);

        assertEquals(controller.getFilmById(1).getLikes().size(), 1);
        assertEquals(Set.of(1), film1.getLikes());

        assertThrows(NotFoundException.class, () -> controller.setLike(9999, 1));
    }

    @Test
    public void shouldRemoveLike() {
        Film film1 = unit.getFilm();

        controller.addFilm(film1);
        controller.setLike(1, 1);

        assertEquals(Set.of(1), film1.getLikes());
        assertThrows(NotFoundException.class, () -> controller.removeLike(999, 1));
        System.out.println(controller.getFilms());
        controller.removeLike(1,1);
        assertEquals(Set.of(), film1.getLikes());
    }

    @Test
    public void shouldGetPopularFilmsList() {
        Film film1 = unit.getFilm();
        Film film2 = unit.getFilm();
        Film film3 = unit.getFilm();

        controller.addFilm(film1);
        controller.addFilm(film2);
        controller.addFilm(film3);

        controller.setLike(3, 1);
        controller.setLike(3, 2);
        controller.setLike(3, 3);

        controller.setLike(2, 2);

        controller.setLike(1, 2);
        controller.setLike(1, 1);

        assertEquals(Set.of(film3, film1, film2), controller.getMostPopularFilm(3));
    }
}