package ru.clevertec.check.service.validator.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.service.validator.AnnotationExecutor;
import ru.clevertec.check.service.validator.annotation.NotEmpty;
import ru.clevertec.check.service.validator.exception.ValidatorException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class NotEmptyExecutorTest {

    private AnnotationExecutor<NotEmpty> annotationExecutor;

    @BeforeEach
    public void init() {
        annotationExecutor = new NotEmptyExecutor(new FieldScannerImpl());
    }

    @Test
    public void execute_allFieldsAreValid() {
        List<String> strings = new ArrayList<>();
        Set<Integer> integers = new HashSet<>();
        strings.add("string");
        integers.add(1);
        ExpectedObject expectedObject = new ExpectedObject(strings, integers);
        List<String> messages = new ArrayList<>();

        annotationExecutor.execute(expectedObject, messages);

        assertTrue(messages.isEmpty());
    }

    @Test
    public void execute_allFieldNotValid() {
        List<String> strings = new ArrayList<>();
        Set<Integer> integers = new HashSet<>();
        ExpectedObject expectedObject = new ExpectedObject(strings, integers);
        List<String> messages = new ArrayList<>();

        annotationExecutor.execute(expectedObject, messages);

        assertArrayEquals(new Object[]{"field1", "field2"}, messages.toArray());
    }


    @Test
    public void execute_oneFieldNotValid() {
        List<String> strings = new ArrayList<>();
        Set<Integer> integers = new HashSet<>();
        strings.add("string");
        ExpectedObject expectedObject = new ExpectedObject(strings, integers);
        List<String> messages = new ArrayList<>();

        annotationExecutor.execute(expectedObject, messages);

        assertArrayEquals(new Object[]{"field2"}, messages.toArray());
    }

    @Test
    public void execute_oneFieldIsNull() {
        List<String> strings = new ArrayList<>();
        strings.add("string");
        ExpectedObject expectedObject = new ExpectedObject(strings, null);
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


    private record ExpectedObject(@NotEmpty(message = "field1") Collection<?> field1,
                                  @NotEmpty(message = "field2") Collection<?> field2) {
    }

    private record ExpectedObjectWithIncorrectField(@NotEmpty(message = "field1") String field1) {
    }

}