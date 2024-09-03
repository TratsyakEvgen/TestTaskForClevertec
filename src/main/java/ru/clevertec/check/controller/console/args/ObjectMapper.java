package ru.clevertec.check.controller.console.args;

public interface ObjectMapper<T> {

    T getObject(String[] arg);

}
