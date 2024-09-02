package ru.clevertec.check.service.validator.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.service.validator.AnnotationExecutor;
import ru.clevertec.check.service.validator.FieldScanner;
import ru.clevertec.check.service.validator.annotation.Min;
import ru.clevertec.check.service.validator.exception.ValidatorException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class MinExecutorTest {

    private AnnotationExecutor<Min> annotationExecutor;
    @Mock
    private FieldScanner fieldScanner;

    @BeforeEach
    void setup() {
        annotationExecutor = new MinExecutor(fieldScanner);
    }

    @ParameterizedTest
    @MethodSource("generateTestObjectAndListFieldAndExpectedMessages")
    void execute(TestObject testObject, List<Field> fields, List<String> expected) {
        List<String> actual = new ArrayList<>();
        Mockito.when(fieldScanner.findAnnotation(Min.class, testObject)).thenReturn(fields);

        annotationExecutor.execute(testObject, actual);

        assertEquals(expected, actual);
    }


    @Test
    public void execute_incorrectField() {
        TestObjectWithIncorrectField testObject = new TestObjectWithIncorrectField("");
        Mockito.when(fieldScanner.findAnnotation(Min.class, testObject))
                .thenReturn(List.of(TestObjectWithIncorrectField.class.getDeclaredFields()));

        assertThrows(ValidatorException.class, () -> annotationExecutor.execute(testObject, Collections.emptyList()));
    }

    static Stream<Arguments> generateTestObjectAndListFieldAndExpectedMessages() {
        Class<?> expectedObjectClass = TestObject.class;
        List<Field> fields = List.of(expectedObjectClass.getDeclaredFields());
        return Stream.of(
                Arguments.of(new TestObject((byte) 1, 2), fields, Collections.emptyList()),
                Arguments.of(new TestObject((byte) 0, 1), fields, List.of("field1", "field2")),
                Arguments.of(new TestObject((byte) 1, 1), fields, List.of("field2")),
                Arguments.of(new TestObject((byte) 1, null), fields, List.of("field2"))
        );
    }


    private record TestObject(@Min(value = 1, message = "field1") byte field1,
                              @Min(value = 2, message = "field2") Integer field2) {
    }

    private record TestObjectWithIncorrectField(@Min(value = 1, message = "field1") String field1) {
    }

}