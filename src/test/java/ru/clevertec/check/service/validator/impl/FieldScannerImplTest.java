package ru.clevertec.check.service.validator.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.service.validator.FieldScanner;
import ru.clevertec.check.service.validator.annotation.NotEmpty;
import ru.clevertec.check.service.validator.annotation.NotNull;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FieldScannerImplTest {
    private FieldScanner fieldScanner;

    @BeforeEach
    public void init() {
        fieldScanner = new FieldScannerImpl();
    }

    @Test
    void findAnnotation_existsAnnotation() throws NoSuchFieldException {
        Class<?> expectedObjectClass = TestObject.class;

        List<Field> actual = fieldScanner.findAnnotation(NotEmpty.class, new TestObject());

        assertEquals(
                List.of(
                        expectedObjectClass.getDeclaredField("field1"),
                        expectedObjectClass.getDeclaredField("field3")
                ),
                actual
        );

    }

    @Test
    void findAnnotation_noExistsAnnotation() {
        List<Field> fields = fieldScanner.findAnnotation(NotNull.class, new TestObject());

        assertTrue(fields.isEmpty());
    }

    private static class TestObject {
        @NotEmpty
        private String field1;
        private String field2;
        @NotEmpty
        private String field3;

    }

}