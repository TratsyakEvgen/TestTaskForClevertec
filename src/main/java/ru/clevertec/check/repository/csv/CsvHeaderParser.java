package ru.clevertec.check.repository.csv;

import java.util.List;

public interface CsvHeaderParser {
    List<Header> parse(String strings, Class<?> aClass);
}
