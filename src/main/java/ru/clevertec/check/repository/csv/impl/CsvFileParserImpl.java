package ru.clevertec.check.repository.csv.impl;

import ru.clevertec.check.ioc.annotation.Inject;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;
import ru.clevertec.check.repository.csv.Csv;
import ru.clevertec.check.repository.csv.CsvFileParser;
import ru.clevertec.check.repository.csv.CsvHeaderParser;
import ru.clevertec.check.repository.csv.Header;
import ru.clevertec.check.repository.csv.exeption.CsvParserException;
import ru.clevertec.check.repository.csv.exeption.HeaderParserException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@NoSpringComponent
public class CsvFileParserImpl implements CsvFileParser {
    @Inject
    private CsvHeaderParser csvHeaderParser;

    public CsvFileParserImpl() {
    }

    public CsvFileParserImpl(CsvHeaderParser csvHeaderParser) {
        this.csvHeaderParser = csvHeaderParser;
    }

    @Override
    public <T> Csv<T> parse(Class<T> aClass, String repository) {


        try {
            Path path = Paths.get(repository);

            List<String> strings = Files.readAllLines(path);
            List<Header> headers = csvHeaderParser.parse(strings.getFirst(), aClass);

            return new Csv<>(strings, headers, aClass);

        } catch (IOException | HeaderParserException e) {
            throw new CsvParserException("Can not parse file " + repository, e);
        }

    }

    ;


}
