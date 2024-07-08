package ru.clevertec.check.service.validator.impl;

import ru.clevertec.check.ioc.annotation.Inject;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;
import ru.clevertec.check.service.validator.AnnotationExecutor;
import ru.clevertec.check.service.validator.FieldScanner;
import ru.clevertec.check.service.validator.Validator;
import ru.clevertec.check.service.validator.ValidatorException;
import ru.clevertec.check.service.validator.annotation.ValidCollection;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

@NoSpringComponent
public class ValidCollectionExecutor implements AnnotationExecutor<ValidCollection> {
    @Inject
    private Validator validator;
    @Inject
    private FieldScanner fieldScanner;

    public ValidCollectionExecutor() {
    }

    public ValidCollectionExecutor(Validator validator, FieldScanner fieldScanner) {
        this.validator = validator;
        this.fieldScanner = fieldScanner;
    }

    @Override
    public void execute(Object object, List<String> message) {

        List<Field> fields = fieldScanner.findAnnotation(ValidCollection.class, object);
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Collection<?> collection = (Collection<?>) field.get(object);
                collection.forEach(element -> message.addAll(validator.validate(element))
                );
            }

        } catch (IllegalAccessException e) {
            throw new ValidatorException("Fail check on Min in " + object, e);
        }

    }

    public Validator getValidator() {
        return validator;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

}
