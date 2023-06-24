package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController controller;
    private UserService userService;

    private static final TestUnits unit = new TestUnits();

    @BeforeEach
    public void setup() {
        userService = new UserService(new InMemoryUserStorage());
        controller = new UserController(userService);
    }

    @Test
    public void shouldAddUser() {
        User user = unit.getUser();

        controller.addUser(user);
        controller.addUser(unit.getUserWithoutName());

        assertEquals(user, controller.getUsersList().get(0));
        assertEquals(user, userService.getUser(1));
        assertEquals("login", userService.getUser(2).getName());
    }

    @Test
    public void shouldGetExceptionIfUserHaveIncorrectField() {
        User user = unit.getUser();
        user.setId(9999);

        assertThrows(RuntimeException.class, () -> controller.addUser(unit.getUserWithIncorrectLogin()));
        assertThrows(RuntimeException.class, () -> controller.addUser(unit.getUserWithIncorrectEmail()));
        assertThrows(RuntimeException.class, () -> controller.addUser(unit.getUserWithIncorrectBirthday()));

        assertThrows(RuntimeException.class, () -> controller.updateUser(unit.getUserWithIncorrectLogin()));
        assertThrows(RuntimeException.class, () -> controller.updateUser(unit.getUserWithIncorrectEmail()));
        assertThrows(RuntimeException.class, () -> controller.updateUser(unit.getUserWithIncorrectBirthday()));
        assertThrows(RuntimeException.class, () -> controller.updateUser(user));
    }

    @Test
    public void shouldUpdateUser() {
        User user = unit.getUser();
        user.setId(1);

        controller.addUser(unit.getUser());
        controller.updateUser(user);

        assertEquals(user, userService.getUser(1));
    }

    @Test
    public void shouldGetUsersList() {
        User user = unit.getUser();

        controller.addUser(user);

        assertEquals(user, controller.getUsersList().get(0));

    }

    @Test
    public void shouldGetUserById() {
        User user1 = unit.getUser();
        User user2 = unit.getUser();
        User user3 = unit.getUser();

        controller.addUser(user1);
        controller.addUser(user2);
        controller.addUser(user3);

        assertEquals(user1, controller.getUserById(1));
        assertEquals(user2, controller.getUserById(2));
        assertEquals(user3, controller.getUserById(3));
        assertThrows(NotFoundException.class, () -> controller.getUserById(999));
    }

    @Test
    public void shouldAddFriend() {
        User user1 = unit.getUser();
        User user2 = unit.getUser();

        controller.addUser(user1);
        controller.addUser(user2);
        controller.addFriend(1, 2);

        assertTrue(user1.getFriends().containsKey(2));
        assertThrows(NotFoundException.class, () -> controller.addFriend(1, 9999));
        assertThrows(NotFoundException.class, () -> controller.addFriend(2, -1));
    }

    @Test
    public void shouldUnfriend() {
        User user1 = unit.getUser();
        User user2 = unit.getUser();

        controller.addUser(user1);
        controller.addUser(user2);
        controller.addFriend(1, 2);

        assertTrue(user1.getFriends().containsKey(2));

        controller.removeFriend(1, 2);

        assertFalse(user1.getFriends().containsKey(2));
        assertThrows(NotFoundException.class, () -> controller.removeFriend(1, 999));
    }

    @Test
    public void shouldGetFriendList() {
        User user1 = unit.getUser();
        User user2 = unit.getUser();
        User user3 = unit.getUser();

        controller.addUser(user1);
        controller.addUser(user2);
        controller.addUser(user3);

        controller.addFriend(1, 2);
        controller.addFriend(1, 3);

        controller.addFriend(2, 1);

        assertEquals(List.of(user2, user3), controller.getFriendsList(1));
        assertEquals(List.of(user1), controller.getFriendsList(2));
    }

    @Test
    public void shouldGetMutualFriendList() {
        User user1 = unit.getUser();
        User user2 = unit.getUser();
        User user3 = unit.getUser();

        controller.addUser(user1);
        controller.addUser(user2);
        controller.addUser(user3);

        controller.addFriend(1,3);
        controller.addFriend(2, 3);

        assertEquals(List.of(user3), controller.getMutualFriends(1, 2));
        assertEquals(List.of(), controller.getMutualFriends(1, 3));
    }
}