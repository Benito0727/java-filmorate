package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends NullPointerException {

    public NotFoundException(final String message) {
        super(message);
    }
}
