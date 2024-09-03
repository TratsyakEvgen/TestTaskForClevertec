package ru.clevertec.check.ioc;

import java.util.Set;

public interface ClassScanner {
    Set<Class<?>> findAllClasses();
}
