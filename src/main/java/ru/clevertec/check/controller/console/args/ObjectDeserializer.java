package ru.clevertec.check.controller.console.args;

public interface ObjectDeserializer<T> {
    T deserializer(String[] strings, String regEx, String split);
}
