package ru.clevertec.check.service.validator.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.service.validator.AnnotationExecutor;
import ru.clevertec.check.service.validator.annotation.Pattern;
import ru.clevertec.check.service.validator.exception.ValidatorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PatternExecutorTest {
    private AnnotationExecutor<Pattern> annotationExecutor;

    @BeforeEach
    public void init() {
        annotationExecutor = new PatternExecutor(new FieldScannerImpl());
    }

    @Test
    public void execute_allFieldsAreValid() {
        ExpectedObject expectedObject = new ExpectedObject("field1", "field2");
        List<String> messages = new ArrayList<>();

        annotationExecutor.execute(expectedObject, messages);

        assertTrue(messages.isEmpty());
    }

    @Test
    public void execute_allFieldNotValid() {
        ExpectedObject expectedObject = new ExpectedObject("", "");
        List<String> messages = new ArrayList<>();

        annotationExecutor.execute(expectedObject, messages);

        assertArrayEquals(new Object[]{"field1", "field2"}, messages.toArray());
    }


    @Test
    public void execute_oneFieldNotValid() {
        ExpectedObject expectedObject = new ExpectedObject("", "field2");
        List<String> messages = new ArrayList<>();

        annotationExecutor.execute(expectedObject, messages);

        assertArrayEquals(new Object[]{"field1"}, messages.toArray());
    }

    @Test
    public void execute_oneFieldIsNull() {
        ExpectedObject expectedObject = new ExpectedObject(null, "field2");
        List<String> messages = new ArrayList<>();

        annotationExecutor.execute(expectedObject, messages);

        assertArrayEquals(new Object[]{"field1"}, messages.toArray());
    }

    @Test
    public void execute_incorrectField() {
        assertThrows(ValidatorException.class,
                () -> annotationExecutor.execute(new ExpectedObjectWithIncorrectField(1), Collections.emptyList())
        );
    }


    private record ExpectedObject(@Pattern(regexp = ".+", message = "field1") String field1,
                                  @Pattern(regexp = ".+", message = "field2") String field2) {
    }

    private record ExpectedObjectWithIncorrectField(@Pattern(regexp = ".+", message = "field1") int field1) {
    }

}