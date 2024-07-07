package ru.clevertec.check.service.validator.impl;


import ru.clevertec.check.ioc.annotation.Inject;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;
import ru.clevertec.check.service.validator.AnnotationExecutor;
import ru.clevertec.check.service.validator.FieldScanner;
import ru.clevertec.check.service.validator.ValidatorException;
import ru.clevertec.check.service.validator.annotation.Min;

import java.lang.reflect.Field;
import java.util.List;

@NoSpringComponent
public class MinExecutor implements AnnotationExecutor<Min> {
    @Inject
    private FieldScanner fieldScanner;

    public MinExecutor() {
    }

    public MinExecutor(FieldScanner fieldScanner) {
        this.fieldScanner = fieldScanner;
    }

    public void execute(Object object, List<String> message) {

        List<Field> fields = fieldScanner.findAnnotation(Min.class, object);
        try {
            for (Field field : fields) {
                Min annotation = field.getAnnotation(Min.class);
                long annotationLong = annotation.value();

                field.setAccessible(true);
                long fieldLong = field.getLong(object);

                if (fieldLong < annotationLong) {
                    message.add(annotation.message());
                }
            }

        } catch (IllegalAccessException e) {
            throw new ValidatorException("Fail check on Min in " + object, e);
        }
    }
}
