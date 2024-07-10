package ru.clevertec.check.service.validator.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.service.validator.AnnotationExecutor;
import ru.clevertec.check.service.validator.Validator;
import ru.clevertec.check.service.validator.annotation.ValidCollection;
import ru.clevertec.check.service.validator.exception.ValidatorException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ValidCollectionExecutorTest {
    @Mock
    private Validator validator;
    private AnnotationExecutor<ValidCollection> annotationExecutor;

    @BeforeEach
    public void init() {
        annotationExecutor = new ValidCollectionExecutor(validator, new FieldScannerImpl());
    }

    @Test
    public void execute_checkNumberOfCalls() {
        List<Object> objects = new ArrayList<>();
        Object obj1 = new Object();
        Object obj2 = new Object();
        objects.add(obj1);
        objects.add(obj2);
        ExpectedObject expectedObject = new ExpectedObject(objects);

        annotationExecutor.execute(expectedObject, Collections.emptyList());

        verify(validator).validate(obj1);
        verify(validator).validate(obj2);
    }

    @Test
    public void execute_incorrectField() {
        assertThrows(ValidatorException.class,
                () -> annotationExecutor.execute(new ExpectedObjectWithIncorrectField(1), Collections.emptyList())
        );
    }


    private record ExpectedObject(@ValidCollection Collection<?> collection) {
    }

    private record ExpectedObjectWithIncorrectField(@ValidCollection int field1) {
    }

}