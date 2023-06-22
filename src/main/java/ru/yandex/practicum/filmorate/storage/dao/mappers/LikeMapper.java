package ru.yandex.practicum.filmorate.storage.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.storage.dao.mappers.databaseEntities.Like;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LikeMapper implements RowMapper<Like> {

    @Override
    public Like mapRow(ResultSet rs, int rowNum) throws SQLException {
        Like like = new Like();

        like.setUserId(rs.getInt("user_id"));

        return like;
    }
}
