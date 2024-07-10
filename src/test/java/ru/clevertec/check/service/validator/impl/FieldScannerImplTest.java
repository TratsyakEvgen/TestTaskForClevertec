package ru.clevertec.check.service.validator.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.service.validator.FieldScanner;
import ru.clevertec.check.service.validator.annotation.NotEmpty;
import ru.clevertec.check.service.validator.annotation.NotNull;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldScannerImplTest {
    private FieldScanner fieldScanner;

    @BeforeEach
    public void init() {
        fieldScanner = new FieldScannerImpl();
    }

    @Test
    public void findAnnotation_existsAnnotation() throws NoSuchFieldException {

        List<Field> fields = fieldScanner.findAnnotation(NotEmpty.class, new ExpectedObject());

        Class<?> clazz = ExpectedObject.class;
        Field[] expected = new Field[]{clazz.getDeclaredField("field1"), clazz.getDeclaredField("field3")};

        assertArrayEquals(expected, fields.toArray());

    }

    @Test
    public void findAnnotation_noExistsAnnotation() {

        List<Field> fields = fieldScanner.findAnnotation(NotNull.class, new ExpectedObject());

        assertTrue(fields.isEmpty());
    }

    private static class ExpectedObject {
        @NotEmpty
        private String field1;
        private String field2;
        @NotEmpty
        private String field3;

    }

}