package ru.clevertec.check.service.validator;

import java.lang.annotation.Annotation;
import java.util.List;

public interface AnnotationExecutor<T extends Annotation> {
    void execute(Object object, List<String> message);
}
