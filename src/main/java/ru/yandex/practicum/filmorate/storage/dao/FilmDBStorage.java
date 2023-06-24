package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dao.mappers.*;
import ru.yandex.practicum.filmorate.storage.dao.mappers.databaseEntities.Like;
import ru.yandex.practicum.filmorate.storage.dao.mappers.databaseEntities.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.mappers.databaseEntities.Genre;
import ru.yandex.practicum.filmorate.storage.dao.mappers.databaseEntities.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("FilmDBStorage")
public class FilmDBStorage implements FilmStorage {

    TreeSet<Film> mostPopularFilms = new TreeSet<>(Comparator.comparingInt(film -> film.getLikes().size()));

    private final JdbcTemplate jdbcTemplate;

    private int filmId = 0;

    @Autowired
    public FilmDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        if ((film.getId() < 1)) {
            List<Integer> filmsIds = getFilmsIds();
            if (!filmsIds.isEmpty()) {
                filmId = Collections.max(filmsIds);
            }
            film.setId(++filmId);
        }
        String sqlQuery = "INSERT INTO FILMS (FILM_ID, TITLE, DESCRIPTION, DURATION, RELEASE_DATE)" +
                " VALUES(?, ?, ?, ?, ?);";
        jdbcTemplate.update(sqlQuery, film.getId(),
                film.getName(), film.getDescription(),
                film.getDuration(), film.getReleaseDate()
        );

        if (film.getMpa() != null) {
            String sqlRating = "INSERT INTO film_rating (film_id, rating_id) VALUES(?, ?)";

            jdbcTemplate.update(sqlRating, film.getId(), film.getMpa().getId());
        }

        if (!film.getGenres().isEmpty()) {
            String sqlGenre = "INSERT INTO film_genre (film_id, genre_id) VALUES(?, ?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlGenre, film.getId(), genre.getId());
            }
        }

        if (!film.getLikes().isEmpty()) {
            String sqlLikes = "INSERT INTO likes (film_id, user_id) VALUES(?, ?)";

            jdbcTemplate.update(sqlLikes, film.getId(), film.getLikes());
        }
        if (getFilm(film.getId()).isPresent()) {
            return getFilm(film.getId()).get();
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public Optional<Film> getFilm(int id) {
        String sqlQuery = "SELECT * " +
                "FROM films " +
                "WHERE film_id=?";

        Film film = jdbcTemplate.query(sqlQuery, new FilmMapper(), id).stream().findAny().orElse(null);

        String sqlMpaQuery = "SELECT f.film_id,\n" +
                "fr.rating_id,\n" +
                "r.name\n" +
                "FROM films AS f\n" +
                "LEFT JOIN film_rating AS fr ON f.film_id=fr.film_id\n" +
                "LEFT JOIN rating AS r ON fr.rating_id=r.rating_id\n" +
                "WHERE f.film_id=?";
        Mpa mpa = jdbcTemplate.query(sqlMpaQuery, new MpaMapper(), id).stream().findAny().orElse(null);

        if (film != null) {
            if (mpa != null) {
                film.setMpa(mpa);
            }
            String sqlGenresQuery = "SELECT fg.film_id, " +
                    "fg.genre_id, " +
                    "g.name " +
                    "FROM film_genre AS fg " +
                    "LEFT JOIN genres AS g ON fg.genre_id=g.genre_id " +
                    "WHERE fg.film_id=? ";

            film.setGenres(new TreeSet<>(jdbcTemplate.query(sqlGenresQuery, new GenreMapper(), id)));

            String sqlLikesQuery = "SELECT user_id FROM likes WHERE film_id=?";
            List<Like> likes = jdbcTemplate.query(sqlLikesQuery, new LikeMapper(), id);
            likes.forEach(like -> film.setLikes(like.getUserId()));
        }
        return Optional.ofNullable(film);
    }

    @Override
    public void removeFilm(int id) {
        String sqlQuery = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        String sqlQuery = "UPDATE films SET film_id=?, title=?, description=?, duration=?, release_date=?" +
                " WHERE film_id = ?";

        jdbcTemplate.update(sqlQuery, film.getId(), film.getName(),
                film.getDescription(), film.getDuration(),
                film.getReleaseDate(), film.getId());

        if (film.getMpa() != null) {
            deleteRating(film);
            String sqlRatingQuery = "INSERT INTO film_rating(film_id, rating_id)" +
                    " VALUES(?, ?)";
            jdbcTemplate.update(sqlRatingQuery, film.getId(), film.getMpa().getId());
        } else {
            deleteRating(film);
        }

        if (!film.getGenres().isEmpty()) {
            deleteGenre(film);
            String sqlGenreQuery = "INSERT INTO film_genre(film_id, genre_id)" +
                    " VALUES(?, ?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlGenreQuery, film.getId(), genre.getId());
            }
        } else {
            deleteGenre(film);
        }

        if (!film.getLikes().isEmpty()) {
            deleteLikes(film);
            String sqlLikesQuery = "INSERT INTO likes(film_id, user_id)" +
                    " VALUES(?, ?)";
            for (Integer like : film.getLikes()) {
                jdbcTemplate.update(sqlLikesQuery, film.getId(), like);
            }

        } else {
            deleteLikes(film);
        }
        return Optional.of(film);
    }

    private void deleteLikes(Film film) {
        String sqlLikesDelete = "DELETE FROM likes WHERE film_id=?";
        jdbcTemplate.update(sqlLikesDelete, film.getId());
    }

    private void deleteRating(Film film) {
        String sqlRatingDelete = "DELETE FROM film_rating WHERE film_id=?";
        jdbcTemplate.update(sqlRatingDelete, film.getId());
    }

    private void deleteGenre(Film film) {
        String sqlGenresDelete = "DELETE FROM film_genre" +
                " WHERE film_id=?";
        jdbcTemplate.update(sqlGenresDelete, film.getId());
    }

    @Override
    public List<Film> getFilmsList() {
        String sqlFilmQuery = "SELECT * FROM films";
        List<Film> films = jdbcTemplate.query(sqlFilmQuery, new FilmMapper());

        for (Film film : films) {
            String sqlMpaQuery = "SELECT fr.rating_id,\n" +
                    "r.name\n" +
                    "FROM films AS f\n" +
                    "LEFT JOIN film_rating AS fr ON f.film_id=fr.film_id\n" +
                    "LEFT JOIN rating AS r ON fr.rating_id=r.rating_id\n" +
                    "WHERE f.film_id=?";
            jdbcTemplate.query(sqlMpaQuery, new MpaMapper(), film.getId()).forEach(film::setMpa);

            String sqlGenresQuery = "SELECT fg.film_id, " +
                    "fg.genre_id, " +
                    "g.name " +
                    "FROM film_genre AS fg " +
                    "LEFT JOIN genres AS g ON fg.genre_id=g.genre_id " +
                    "WHERE fg.film_id=? ";

            film.setGenres(new TreeSet<>(jdbcTemplate.query(sqlGenresQuery, new GenreMapper(), film.getId())));

            String sqlLikesQuery = "SELECT user_id FROM likes WHERE film_id=?";
            List<Like> likes = jdbcTemplate.query(sqlLikesQuery, new LikeMapper(), film.getId());
            likes.forEach(like -> film.setLikes(like.getUserId()));
        }
        return films;
    }

    @Override
    public Set<Film> getMostPopularFilms() {
        List<Film> films = getFilmsList();

        mostPopularFilms.addAll(films);

        return mostPopularFilms.descendingSet();
    }

    @Override
    public Set<Genre> getGenres() {
        String sqlQuery = "SELECT *" +
                " FROM genres";
        return new TreeSet<>(jdbcTemplate.query(sqlQuery, new GenreMapper()));
    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        String sqlQuery = "SELECT *" +
                " FROM genres" +
                " WHERE genre_id=?";
        return jdbcTemplate.query(sqlQuery, new GenreMapper(), id).stream().findAny();
    }

    @Override
    public Set<Rating> getRatings() {
        String sqlQuery = "SELECT *" +
                " FROM rating";

        return new TreeSet<>(jdbcTemplate.query(sqlQuery, new RatingMapper()));
    }

    @Override
    public Optional<Rating> getRatingById(int id) {
        String sqlQuery = "SELECT *" +
                " FROM rating" +
                " WHERE rating_id = ?";
        return jdbcTemplate.query(sqlQuery, new RatingMapper(), id).stream().findAny();
    }

    private List<Integer> getFilmsIds() {
        String sqlQuery = "SELECT * " +
                "FROM films;";
        return jdbcTemplate.query(sqlQuery, (rs, rowNun) -> getId(rs));

    }

    private int getId(ResultSet rs) throws SQLException {
        return rs.getInt("film_id");
    }
}
