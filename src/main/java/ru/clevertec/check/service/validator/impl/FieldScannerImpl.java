package ru.clevertec.check.service.validator.impl;

import ru.clevertec.check.ioc.annotation.NoSpringComponent;
import ru.clevertec.check.service.validator.FieldScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@NoSpringComponent
public class FieldScannerImpl implements FieldScanner {
    @Override
    public List<Field> findAnnotation(Class<? extends Annotation> aClass, Object object) {
        return Arrays.stream(object.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(aClass))
                .toList();
    }
}
