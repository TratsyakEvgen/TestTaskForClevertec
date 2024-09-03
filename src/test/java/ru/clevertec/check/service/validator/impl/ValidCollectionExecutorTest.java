package ru.clevertec.check.service.validator.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.service.validator.AnnotationExecutor;
import ru.clevertec.check.service.validator.FieldScanner;
import ru.clevertec.check.service.validator.Validator;
import ru.clevertec.check.service.validator.annotation.ValidCollection;
import ru.clevertec.check.service.validator.exception.ValidatorException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidCollectionExecutorTest {

    private AnnotationExecutor<ValidCollection> annotationExecutor;


    @Mock
    private Validator validator;

    @Mock
    private FieldScanner fieldScanner;


    @BeforeEach
    public void setup() {
        annotationExecutor = new ValidCollectionExecutor(validator, fieldScanner);
    }

    @Test
    public void execute_checkNumberOfCalls() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        List<Object> objects = List.of(obj1, obj2);
        TestObject testObject = new TestObject(objects);

        when(fieldScanner.findAnnotation(ValidCollection.class, testObject))
                .thenReturn(List.of(TestObject.class.getDeclaredFields()));

        annotationExecutor.execute(testObject, Collections.emptyList());

        verify(validator).validate(obj1);
        verify(validator).validate(obj2);
    }

    @Test
    public void execute_incorrectField() {
        TestObjectWithIncorrectField testObject = new TestObjectWithIncorrectField(1);
        when(fieldScanner.findAnnotation(ValidCollection.class, testObject))
                .thenReturn(List.of(TestObjectWithIncorrectField.class.getDeclaredFields()));

        assertThrows(ValidatorException.class, () -> annotationExecutor.execute(testObject, Collections.emptyList()));
    }


    private record TestObject(@ValidCollection Collection<?> collection) {
    }

    private record TestObjectWithIncorrectField(@ValidCollection int field1) {
    }

}