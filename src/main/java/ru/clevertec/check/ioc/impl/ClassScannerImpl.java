package ru.clevertec.check.ioc.impl;

import ru.clevertec.check.ioc.ClassScanner;
import ru.clevertec.check.ioc.exception.ClassScannerException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassScannerImpl implements ClassScanner {
    @Override
    public Set<Class<?>> findAllClasses() {

        String realPath = System.getProperty("java.class.path").split(File.pathSeparator)[0] + File.separator;

        try (Stream<Path> stream = Files.walk(Paths.get(realPath))) {

            return stream.filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(path -> path.endsWith(".class"))
                    .map(path -> {

                        String className = getClassName(path, realPath);
                        try {
                            return Class.forName(className);
                        } catch (ClassNotFoundException e) {
                            throw new ClassScannerException("Can't get class by name " + className, e);
                        }

                    })
                    .collect(Collectors.toSet());

        } catch (IOException e) {
            throw new ClassScannerException("Can't get directories in " + realPath, e);
        }


    }


    private String getClassName(String path, String realPath) {
        return path.replace(realPath, "")
                .replace(".class", "")
                .replace(File.separator, ".");
    }

}
