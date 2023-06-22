package ru.yandex.practicum.filmorate.storage.dao.mappers;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.storage.dao.mappers.databaseEntities.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MpaMapper implements RowMapper<Mpa> {

    @Override
    public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        Mpa mpa = new Mpa();

        mpa.setId(rs.getInt("rating_id"));
        mpa.setName(rs.getString("name"));
        return mpa;
    }
}
