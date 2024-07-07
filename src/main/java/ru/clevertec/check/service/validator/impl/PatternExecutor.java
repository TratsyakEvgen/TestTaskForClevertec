package ru.clevertec.check.service.validator.impl;

import ru.clevertec.check.ioc.annotation.Inject;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;
import ru.clevertec.check.service.validator.AnnotationExecutor;
import ru.clevertec.check.service.validator.FieldScanner;
import ru.clevertec.check.service.validator.ValidatorException;
import ru.clevertec.check.service.validator.annotation.Pattern;

import java.lang.reflect.Field;
import java.util.List;

@NoSpringComponent
public class PatternExecutor implements AnnotationExecutor<Pattern> {
    @Inject
    private FieldScanner fieldScanner;

    public PatternExecutor() {
    }

    public PatternExecutor(FieldScanner fieldScanner) {
        this.fieldScanner = fieldScanner;
    }

    @Override
    public void execute(Object object, List<String> message) {
        List<Field> fields = fieldScanner.findAnnotation(Pattern.class, object);
        try {
            for (Field field : fields) {
                Pattern annotation = field.getAnnotation(Pattern.class);
                String regex = annotation.regexp();

                field.setAccessible(true);
                String value = (String) field.get(object);


                if (!value.matches(regex)) {
                    message.add(annotation.message());
                }
            }

        } catch (IllegalAccessException e) {
            throw new ValidatorException("Fail check on Pattern in " + object, e);
        }
    }
}
