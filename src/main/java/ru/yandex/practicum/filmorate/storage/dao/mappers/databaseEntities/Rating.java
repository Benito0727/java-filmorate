package ru.yandex.practicum.filmorate.storage.dao.mappers.databaseEntities;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class Rating implements Comparable<Rating> {

    private Integer id;

    private String name;

    public Rating() {
    }

    public Rating(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int compareTo(@NotNull Rating o) {
        return this.id.compareTo(o.id);
    }
}
