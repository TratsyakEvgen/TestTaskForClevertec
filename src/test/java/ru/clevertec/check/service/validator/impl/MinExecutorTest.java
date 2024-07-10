package ru.clevertec.check.service.validator.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.service.validator.AnnotationExecutor;
import ru.clevertec.check.service.validator.annotation.Min;
import ru.clevertec.check.service.validator.exception.ValidatorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MinExecutorTest {

    private AnnotationExecutor<Min> annotationExecutor;

    @BeforeEach
    public void init() {
        annotationExecutor = new MinExecutor(new FieldScannerImpl());
    }

    @Test
    public void execute_allFieldsAreValid() {
        ExpectedObject expectedObject = new ExpectedObject((byte) 1, 2);
        List<String> messages = new ArrayList<>();

        annotationExecutor.execute(expectedObject, messages);
        assertTrue(messages.isEmpty());
    }

    @Test
    public void execute_allFieldNotValid() {
        ExpectedObject expectedObject = new ExpectedObject((byte) 0, 1);
        List<String> messages = new ArrayList<>();

        annotationExecutor.execute(expectedObject, messages);

        assertArrayEquals(new Object[]{"field1", "field2"}, messages.toArray());
    }


    @Test
    public void execute_oneFieldNotValid() {
        ExpectedObject expectedObject = new ExpectedObject((byte) 1, 1);
        List<String> messages = new ArrayList<>();

        annotationExecutor.execute(expectedObject, messages);

        assertArrayEquals(new Object[]{"field2"}, messages.toArray());
    }


    @Test
    public void execute_incorrectField() {
        assertThrows(ValidatorException.class,
                () -> annotationExecutor.execute(new ExpectedObjectWithIncorrectField("Test"), Collections.emptyList())
        );
    }


    private record ExpectedObject(@Min(value = 1, message = "field1") byte field1,
                                  @Min(value = 2, message = "field2") Integer field2) {
    }

    private record ExpectedObjectWithIncorrectField(@Min(value = 1, message = "field1") String field1) {
    }

}