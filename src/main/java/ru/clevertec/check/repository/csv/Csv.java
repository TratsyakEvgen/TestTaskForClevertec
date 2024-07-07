package ru.clevertec.check.repository.csv;


import ru.clevertec.check.config.ApplicationConfig;
import ru.clevertec.check.repository.csv.exeption.CsvParserException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

public class Csv<T> implements Iterable<T> {
    private final List<String> strings;
    private final List<Header> headers;
    private final Class<T> clazz;
    private int current = 1;

    public Csv(List<String> strings, List<Header> headers, Class<T> clazz) {
        this.strings = strings;
        this.headers = headers;
        this.clazz = clazz;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return current < strings.size();
            }

            @Override
            public T next() {

                try {
                    T entity = clazz.getDeclaredConstructor().newInstance();
                    String currentString = strings.get(current);
                    String[] values = currentString.split(ApplicationConfig.CSV_SEPARATOR);

                    for (Header header : headers) {

                        String value;
                        int columIndex = header.getPosition();

                        try {
                            value = values[columIndex];
                        } catch (IndexOutOfBoundsException e) {
                            throw new CsvParserException(
                                    String.format("In string %s not found value for header %s", currentString, header), e
                            );
                        }


                        Object fieldValue = header.getConverter().convert(value);
                        Field field = header.getField();
                        field.setAccessible(true);
                        field.set(entity, fieldValue);

                    }
                    current++;
                    return entity;

                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new CsvParserException("Can not create entity", e);
                }
            }
        };
    }
}
