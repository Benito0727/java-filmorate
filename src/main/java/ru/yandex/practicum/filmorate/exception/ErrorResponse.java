package ru.yandex.practicum.filmorate.exception;
public class ErrorResponse {

    private final String description;

    private final String error;

    public ErrorResponse(String  error, String description) {
        this.description = description;
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public String getDescription() {
        return description;
    }
}