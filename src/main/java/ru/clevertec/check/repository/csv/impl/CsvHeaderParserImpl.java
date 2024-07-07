package ru.clevertec.check.repository.csv.impl;

import ru.clevertec.check.config.ApplicationConfig;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;
import ru.clevertec.check.repository.csv.Convertor;
import ru.clevertec.check.repository.csv.CsvHeaderParser;
import ru.clevertec.check.repository.csv.Header;
import ru.clevertec.check.repository.csv.annotation.Csv;
import ru.clevertec.check.repository.csv.exeption.HeaderParserException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@NoSpringComponent
public class CsvHeaderParserImpl implements CsvHeaderParser {

    @Override
    public List<Header> parse(String string, Class<?> aClass) {

        String[] splitHeaders = string.split(ApplicationConfig.CSV_SEPARATOR);

        List<Header> headers = new ArrayList<>();

        for (int i = 0; i < splitHeaders.length; i++) {
            headers.add(getHeader(aClass, splitHeaders, i));
        }

        return headers;


    }

    private Header getHeader(Class<?> aClass, String[] splitHeaders, int i) {

        for (Field field : aClass.getDeclaredFields()) {

            if (field.isAnnotationPresent(Csv.class)) {

                Csv csv = field.getAnnotation(Csv.class);

                if (csv.column().equals(splitHeaders[i])) {

                    Class<?> classConvertor = csv.converter();
                    try {

                        Convertor convertor = (Convertor) classConvertor.getConstructor().newInstance();
                        return Header.builder().position(i).converter(convertor).field(field).build();

                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        throw new HeaderParserException("Can not create convertor " + classConvertor, e);
                    }
                }
            }
        }

        throw new HeaderParserException("Not found header: " + splitHeaders[i]);

    }

}
