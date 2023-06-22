package ru.yandex.practicum.filmorate.storage.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {

        User user = new User();

        user.setId(rs.getInt("user_id"));
        user.setLogin(rs.getString("login"));
        user.setEmail(rs.getString("email"));
        user.setName(rs.getString("name"));
        user.setBirthday(LocalDate.parse(rs.getDate("birthday_date").
                toLocalDate().
                format(DateTimeFormatter.
                        ofPattern("yyyy-MM-dd"))));

        return user;
    }
}
