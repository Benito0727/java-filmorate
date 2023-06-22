package ru.yandex.practicum.filmorate.storage.dao.mappers.databaseEntities;

import lombok.Data;

@Data
public class Like {

    private Integer userId;

    private Integer filmId;
}
