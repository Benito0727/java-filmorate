package ru.yandex.practicum.filmorate.controllers;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class TestUnits {

    private Film film;
    private User user;

    public Film getFilm() {
        film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setDuration(120);
        film.setReleaseDate(getFilmDate());
        return film;
    }

    public Film getFilmWithIncorrectName() {
        film = new Film();
        film.setName("");
        film.setDescription("description");
        film.setDuration(120);
        film.setReleaseDate(getFilmDate());
        return film;
    }

    public Film getFilmWithIncorrectDescription() {
        film = new Film();
        film.setName("name");
        film.setDescription("1992 год. Семнадцатилетний Мартин снимает свою повседневную жизнь камерой формата Hi8." +
                " Он снимает все подряд: свою комнату, окружающий его мир... Но в кадр никогда не попадает его отец." +
                " Он об этом просто не думает. Однажды он знакомится с Домиником, которому двадцать три года, и который работает «надзирателем» в лицее.");
        film.setDuration(120);
        film.setReleaseDate(getFilmDate());
        return film;
    }

    public Film getFilmWithIncorrectReleaseDate() {
        film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setDuration(120);
        film.setReleaseDate(LocalDate.of(1894, 1, 1));
        return film;
    }

    public Film getFilmWithIncorrectDuration() {
        film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setDuration(-5);
        film.setReleaseDate(getFilmDate());
        return film;
    }

    public User getUser() {
        user = new User();
        user.setName("Name");
        user.setLogin("login");
        user.setEmail("user@yandex.ru");
        user.setBirthday(getUserDate());
        return user;
    }

    public User getUserWithIncorrectLogin() {
        user = new User();
        user.setName("name");
        user.setLogin("");
        user.setEmail("user@yandex.ru");
        user.setBirthday(getUserDate());
        return user;
    }

    public User getUserWithIncorrectEmail() {
        user = new User();
        user.setName("user");
        user.setLogin("login");
        user.setEmail("asdasd");
        user.setBirthday(getUserDate());
        return user;
    }

    public User getUserWithIncorrectBirthday() {
        user = new User();
        user.setName("name");
        user.setLogin("login");
        user.setEmail("user@yandex.ru");
        user.setBirthday(LocalDate.of(2040, 5, 15));
        return user;
    }

    public User getUserWithoutName() {
        user = new User();
        user.setLogin("login");
        user.setEmail("user@yandex.ru");
        user.setBirthday(getUserDate());
        return user;
    }

    private LocalDate getUserDate() {
        return LocalDate.of(1993, 7, 27);
    }

    private LocalDate getFilmDate() {
        return LocalDate.of(1992, 5, 20);
    }
}
