package ru.clevertec.check.service.validator.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.service.validator.AnnotationExecutor;
import ru.clevertec.check.service.validator.annotation.NotNull;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotNullExecutorTest {
    private AnnotationExecutor<NotNull> annotationExecutor;

    @BeforeEach
    public void init() {
        annotationExecutor = new NotNullExecutor(new FieldScannerImpl());
    }

    @Test
    public void execute_allFieldsAreValid() {
        String string = "test";
        Set<Integer> integers = new HashSet<>();
        integers.add(1);
        ExpectedObject expectedObject = new ExpectedObject(string, integers);
        List<String> messages = new ArrayList<>();

        annotationExecutor.execute(expectedObject, messages);

        assertTrue(messages.isEmpty());
    }

    @Test
    public void execute_allFieldNotValid() {
        ExpectedObject expectedObject = new ExpectedObject(null, null);
        List<String> messages = new ArrayList<>();

        annotationExecutor.execute(expectedObject, messages);

        assertArrayEquals(new Object[]{"field1", "field2"}, messages.toArray());
    }


    @Test
    public void execute_oneFieldNotValid() {
        Set<Integer> integers = new HashSet<>();
        ExpectedObject expectedObject = new ExpectedObject(null, integers);
        List<String> messages = new ArrayList<>();

        annotationExecutor.execute(expectedObject, messages);

        assertArrayEquals(new Object[]{"field1"}, messages.toArray());
    }

    @Test
    public void execute_incorrectField() {
        ExpectedObjectWithIncorrectField expectedObject = new ExpectedObjectWithIncorrectField(0);
        List<String> messages = new ArrayList<>();

        annotationExecutor.execute(expectedObject, messages);

        assertTrue(messages.isEmpty());
    }


    private record ExpectedObject(@NotNull(message = "field1") Object field1,
                                  @NotNull(message = "field2") Collection<?> field2) {
    }

    private record ExpectedObjectWithIncorrectField(@NotNull(message = "field1") int field1) {
    }

}