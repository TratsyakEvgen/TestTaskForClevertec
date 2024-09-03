package ru.clevertec.check.service.validator;

import java.util.List;

public interface Validator {
    <T> List<String> validate(T obj);
}
