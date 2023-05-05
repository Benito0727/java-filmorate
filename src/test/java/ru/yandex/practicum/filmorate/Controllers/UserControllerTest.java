package ru.yandex.practicum.filmorate.Controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController controller;

    private final static TestUnits unit = new TestUnits();

    @BeforeEach
    public void setup() {
        controller = new UserController();
    }

    @Test
    public void shouldAddUser() {
        User user = unit.getUser();

        controller.addUser(user);
        controller.addUser(unit.getUserWithoutName());

        assertEquals(user, controller.getUsersList().get(0));
        assertEquals(user, controller.users.get(1));
        assertEquals("login", controller.users.get(2).getName());
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

        assertEquals(user, controller.users.get(1));
    }

    @Test
    public void shouldGetUsersList() {
        User user = unit.getUser();

        controller.addUser(user);

        assertEquals(user, controller.getUsersList().get(0));

    }
}