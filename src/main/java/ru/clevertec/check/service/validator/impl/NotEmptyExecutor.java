package ru.clevertec.check.service.validator.impl;

import ru.clevertec.check.ioc.annotation.Inject;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;
import ru.clevertec.check.service.validator.AnnotationExecutor;
import ru.clevertec.check.service.validator.FieldScanner;
import ru.clevertec.check.service.validator.ValidatorException;
import ru.clevertec.check.service.validator.annotation.NotEmpty;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

@NoSpringComponent
public class NotEmptyExecutor implements AnnotationExecutor<NotEmpty> {
    @Inject
    private FieldScanner fieldScanner;

    public NotEmptyExecutor() {
    }

    public NotEmptyExecutor(FieldScanner fieldScanner) {
        this.fieldScanner = fieldScanner;
    }

    @Override
    public void execute(Object object, List<String> message) {
        List<Field> fields = fieldScanner.findAnnotation(NotEmpty.class, object);
        try {
            for (Field field : fields) {
                NotEmpty annotation = field.getAnnotation(NotEmpty.class);

                field.setAccessible(true);
                Collection<?> collection = (Collection<?>) field.get(object);


                if (collection == null || collection.isEmpty()) {
                    message.add(annotation.message());
                }
            }

        } catch (IllegalAccessException e) {
            throw new ValidatorException("Fail check on Pattern in " + object, e);
        }
    }
}
