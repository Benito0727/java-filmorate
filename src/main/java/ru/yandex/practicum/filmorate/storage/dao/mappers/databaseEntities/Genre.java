package ru.yandex.practicum.filmorate.storage.dao.mappers.databaseEntities;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class Genre implements Comparable<Genre> {

    private Integer id;

    private String name;

    public Genre() {
    }

    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int compareTo(@NotNull Genre o) {
        return this.id.compareTo(o.id);
    }
}
