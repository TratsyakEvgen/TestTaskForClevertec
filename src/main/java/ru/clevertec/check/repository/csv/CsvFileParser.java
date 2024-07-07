package ru.clevertec.check.repository.csv;

public interface CsvFileParser {

    <T> Csv<T> parse(Class<T> aClass, String repository);
}
