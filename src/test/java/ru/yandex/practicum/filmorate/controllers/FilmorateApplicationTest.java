package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FilmDBStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserDBStorage;
import ru.yandex.practicum.filmorate.storage.dao.mappers.databaseEntities.Genre;
import ru.yandex.practicum.filmorate.storage.dao.mappers.databaseEntities.Rating;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource("classpath:application-test.properties")
@Sql(scripts = {"classpath:db/schema.sql", "classpath:db/data.sql"})
public class FilmorateApplicationTest {


    @Autowired
    private final JdbcTemplate template = new JdbcTemplate();

    @Autowired
    private final UserDBStorage userStorage = new UserDBStorage(template);

    @Autowired
    private final FilmDBStorage filmStorage = new FilmDBStorage(template);

    private final TestUnits units = new TestUnits();

    @Test
    public void testUserStorage() {
        User user1 = userStorage.addUser(units.getUser());

        Optional<User> userOptional = userStorage.getUser(1);

        List<User> userList = userStorage.getUserList();

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );

        assertThat(!userList.isEmpty()).isTrue();
        assertThat(userList.get(0).equals(user1)).isTrue();

        User user2 = userStorage.addUser(units.getUser());

        userList = userStorage.getUserList();

        assertThat(userList.get(1).equals(user2)).isTrue();
        assertThat(userList.size() == 2).isTrue();

        user1.setName("otherName");
        userStorage.updateUser(user1);
        userList = userStorage.getUserList();

        assertThat(user1.getName().equals("otherName")).isTrue();

        assertThat(userList.get(0).equals(user1)).isTrue();
    }

    @Test
    public void testFilmStorage() {
        Film film = units.getFilm();
        filmStorage.addFilm(film);
        Optional<Film> optionalFilm = filmStorage.getFilm(1);

        assertThat(optionalFilm)
                .isPresent()
                .hasValueSatisfying(film1 ->
                        assertThat(film1).hasFieldOrPropertyWithValue("id", 1));

        film.setName("otherName");
        filmStorage.updateFilm(film);
        optionalFilm = filmStorage.getFilm(1);
        assertThat(optionalFilm)
                .isPresent()
                .hasValueSatisfying(film1 ->
                        assertThat(film1).hasFieldOrPropertyWithValue("name", "otherName"));

        List<Film> filmList = filmStorage.getFilmsList();

        assertThat(filmList.size() == 1).isTrue();
        assertThat(filmList.get(0).equals(optionalFilm.get())).isTrue();

        Set<Genre> genres = filmStorage.getGenres();
        System.out.println(genres);
        assertThat(genres.size() == 6).isTrue();
        Set<Genre> genreSet = Set.of(
                new Genre(1, "COMEDY"),
                new Genre(2, "DRAMA"),
                new Genre(3, "CARTOON"),
                new Genre(4, "THRILLER"),
                new Genre(5, "DOCUMENTARY"),
                new Genre(6, "ACTION")
        );

        assertThat(genres.equals(genreSet)).isTrue();

        Set<Rating> ratings = filmStorage.getRatings();
        Set<Rating> ratingSet = Set.of(
                new Rating(1, "G"),
                new Rating(2, "PG"),
                new Rating(3, "PG-13"),
                new Rating(4, "R"),
                new Rating(5, "NC-17")
        );
        assertThat(ratings.size() == 5).isTrue();
        assertThat(ratings.equals(ratingSet)).isTrue();

        Optional<Genre> optionalGenre = filmStorage.getGenreById(2);

        assertThat(optionalGenre)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 2))
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "DRAMA"));

        Optional<Rating> optionalRating = filmStorage.getRatingById(4);

        assertThat(optionalRating)
                .isPresent()
                .hasValueSatisfying(rating ->
                        assertThat(rating).hasFieldOrPropertyWithValue("id", 4))
                .hasValueSatisfying(rating ->
                        assertThat(rating).hasFieldOrPropertyWithValue("name", "R"));
    }



}
