package ru.yandex.practicum.filmorate.storage.dao.mappers.databaseEntities;

import lombok.Data;

@Data
public class Friend {

    private Integer friendUserID;

    private Boolean isFriend;

    public Friend(Integer friendUserID, Boolean isFriend) {
        this.friendUserID = friendUserID;
        this.isFriend = isFriend;
    }
}
