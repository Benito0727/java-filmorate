package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dao.mappers.UserMapper;
import ru.yandex.practicum.filmorate.storage.dao.mappers.databaseEntities.Friend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component("UserDBStorage")
@Slf4j
public class UserDBStorage implements UserStorage {

    private int userId = 0;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        if (user.getId() < 1) {
            List<Integer> usersId = getUsersId();
            if (!usersId.isEmpty()) {
                userId = Collections.max(usersId);
            }
            user.setId(++userId);
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        String sqlUser = "INSERT INTO users (user_id, email, login, name, birthday_date)" +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlUser, user.getId(),
                user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday());

        if (!user.getFriends().isEmpty()) {
            String sqlFriends = "INSERT INTO friends (user_id, friend_id, friendship)" +
                    "VALUES(?, ?, ?)";
            jdbcTemplate.update(sqlFriends, user.getId(), user.getFriends());
        }

        return user;
    }

    @Override
    public Optional<User> getUser(int id) {
        String sqlUsersQuery = "SELECT *" +
                " FROM users AS u" +
                " WHERE user_id = ?";
        User user = jdbcTemplate.query(sqlUsersQuery, new UserMapper(), id).stream().findAny().orElse(null);

        if (user != null) {
            String sqlFriendsQuery = "SELECT friend_user_id, friendship" +
                    " FROM friends" +
                    " WHERE user_id=?";
            try {
                List<Friend> friendList = jdbcTemplate.query(sqlFriendsQuery, (rs, rowNum) -> makeFriendMap(rs), id);
                HashMap<Integer, Boolean> friendMap = new HashMap<>();
                for (Friend friend : friendList) {
                    friendMap.put(friend.getFriendUserID(), friend.getIsFriend());
                }
                user.setFriends(friendMap);
            } catch (BadSqlGrammarException e) {
                log.warn(String.valueOf(e.getSQLException()));
            }
            return Optional.of(user);
        } else {
            throw new NotFoundException(String.format("Не смогли найти юзера с ID %d", id));
        }
    }

    @Override
    public User removeUser(User user) {
        String sqlQuery = "DELETE " +
                "FROM users" +
                "WHERE user_id=?";
        jdbcTemplate.update(sqlQuery, user.getId());
        return user;
    }

    @Override
    public Optional<User> updateUser(User user) {

        String sqlQuery = "UPDATE users" +
                " SET user_id=?, email=?,login=?, name=?, birthday_date=?" +
                " WHERE user_id=?";

        jdbcTemplate.update(sqlQuery,
                        user.getId(),
                        user.getEmail(),
                        user.getLogin(),
                        user.getName(),
                        user.getBirthday(),
                        user.getId());

        String sqlDelete = "DELETE" +
                " FROM friends" +
                " WHERE user_id=?";
        jdbcTemplate.update(sqlDelete, user.getId());

        String sqlUpdateFriend = "INSERT INTO friends(user_id, friend_user_id, friendship) " +
                "VALUES(?, ?, ?)";
        for (Integer integer : user.getFriends().keySet()) {
            jdbcTemplate.update(sqlUpdateFriend, user.getId(), integer, user.getFriends().get(integer));
        }

        return getUser(user.getId());
    }

    @Override
    public List<User> getUserList() {
        String sqlQuery = "SELECT *" +
                "FROM users";
        return jdbcTemplate.query(sqlQuery, new UserMapper());
    }

    private Friend makeFriendMap(ResultSet rs) throws SQLException {
        return new Friend(rs.getInt("friend_user_id"), rs.getBoolean("friendship"));
    }

    private List<Integer> getUsersId() {
        String sqlQuery = "SELECT user_id" +
                " FROM users";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> getId(rs));
    }

    private int getId(ResultSet rs) throws SQLException {
        return rs.getInt("user_id");
    }
}
