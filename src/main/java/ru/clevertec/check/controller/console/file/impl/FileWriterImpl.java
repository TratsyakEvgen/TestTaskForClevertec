package ru.clevertec.check.controller.console.file.impl;

import ru.clevertec.check.controller.console.file.FileWriter;
import ru.clevertec.check.controller.console.file.FileWriterException;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@NoSpringComponent
public class FileWriterImpl implements FileWriter {
    @Override
    public void write(String filePath, byte[] bytes) {
        Path path = Paths.get(filePath);
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            Files.write(path, bytes);

        } catch (IOException e) {
            throw new FileWriterException("Can not write to file: " + filePath, e);
        }
    }
}
