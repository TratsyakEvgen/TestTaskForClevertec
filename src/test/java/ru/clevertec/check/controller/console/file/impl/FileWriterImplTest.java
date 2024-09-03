package ru.clevertec.check.controller.console.file.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.controller.console.file.FileWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileWriterImplTest {

    public static final String TEXT = "some text";
    private static final String FILE_NAME = "some name";
    private static final Path PATH = Paths.get(FILE_NAME);
    private FileWriter fileWriter;


    @BeforeEach
    void setUp() {
        fileWriter = new FileWriterImpl();
    }

    @Test
    void write_ifFileNotExists() throws IOException {
        fileWriter.write(FILE_NAME, TEXT.getBytes());

        List<String> strings = Files.readAllLines(PATH);

        assertTrue(Files.exists(PATH));
        assertTrue(strings.stream().allMatch(string -> string.equals(TEXT)));
    }

    @Test
    void write_ifFileExists() throws IOException {
        Files.write(PATH, "exists".getBytes());

        fileWriter.write(FILE_NAME, TEXT.getBytes());

        List<String> strings = Files.readAllLines(PATH);

        assertTrue(Files.exists(PATH));
        assertTrue(strings.stream().allMatch(string -> string.equals(TEXT)));
    }

    @AfterEach
    void delete() throws IOException {
        Files.delete(Paths.get(FILE_NAME));
    }
}