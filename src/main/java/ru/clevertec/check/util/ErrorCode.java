package ru.clevertec.check.util;

public enum ErrorCode {
    BAD_REQUEST("BAD REQUEST"),
    NOT_ENOUGH_MONEY("NOT ENOUGH MONEY"),
    INTERNAL_SERVER_ERROR("INTERNAL SERVER ERROR");

    private final String title;

    ErrorCode(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
