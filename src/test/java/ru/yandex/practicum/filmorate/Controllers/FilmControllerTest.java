package ru.yandex.practicum.filmorate.Controllers;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class FilmControllerTest {

    private FilmController controller;
    private static final TestUnits unit = new TestUnits();

    @BeforeEach
    public void setup() {
        controller = new FilmController();
    }

    @Test
    public void shouldAddFilm() {
        Film film = unit.getFilm();
        controller.addFilm(film);

       assertEquals(film, controller.films.get(1));
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
    }

    @Test
    public void shouldUpdateFilm() {
        Film film = unit.getFilm();
        film.setId(1);
        controller.addFilm(unit.getFilm());

        controller.updateFilm(film);

        assertEquals(film, controller.getFilms().get(0));
    }

}