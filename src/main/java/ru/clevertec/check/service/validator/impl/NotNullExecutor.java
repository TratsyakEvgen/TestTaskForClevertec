package ru.clevertec.check.service.validator.impl;

import ru.clevertec.check.ioc.annotation.Inject;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;
import ru.clevertec.check.service.validator.AnnotationExecutor;
import ru.clevertec.check.service.validator.FieldScanner;
import ru.clevertec.check.service.validator.annotation.NotNull;
import ru.clevertec.check.service.validator.exception.ValidatorException;

import java.lang.reflect.Field;
import java.util.List;

@NoSpringComponent
public class NotNullExecutor implements AnnotationExecutor<NotNull> {
    @Inject
    private FieldScanner fieldScanner;

    public NotNullExecutor() {
    }

    public NotNullExecutor(FieldScanner fieldScanner) {
        this.fieldScanner = fieldScanner;
    }

    @Override
    public void execute(Object object, List<String> message) {
        List<Field> fields = fieldScanner.findAnnotation(NotNull.class, object);
        try {
            for (Field field : fields) {
                NotNull annotation = field.getAnnotation(NotNull.class);

                field.setAccessible(true);
                Object o = field.get(object);


                if (o == null) {
                    message.add(annotation.message());
                }
            }

        } catch (IllegalAccessException e) {
            throw new ValidatorException("Fail check on Pattern in " + object, e);
        }
    }
}
