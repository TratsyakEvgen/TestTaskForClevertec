package ru.clevertec.check.service.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public interface FieldScanner {
    List<Field> findAnnotation(Class<? extends Annotation> aClass, Object object);
}
