package ru.clevertec.check.service.validator.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.service.validator.AnnotationExecutor;
import ru.clevertec.check.service.validator.FieldScanner;
import ru.clevertec.check.service.validator.annotation.NotNull;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotNullExecutorTest {
    private AnnotationExecutor<NotNull> annotationExecutor;

    @Mock
    private FieldScanner fieldScanner;

    @BeforeEach
    void setup() {
        annotationExecutor = new NotNullExecutor(fieldScanner);
    }


    @ParameterizedTest
    @MethodSource("generateTestObjectAndListFieldAndExpectedMessages")
    void execute(TestObject testObject, List<Field> fields, List<String> expected) {
        List<String> actual = new ArrayList<>();
        when(fieldScanner.findAnnotation(NotNull.class, testObject)).thenReturn(fields);

        annotationExecutor.execute(testObject, actual);

        assertEquals(expected, actual);
    }


    static Stream<Arguments> generateTestObjectAndListFieldAndExpectedMessages() {
        Class<?> expectedObjectClass = TestObject.class;
        List<Field> fields = List.of(expectedObjectClass.getDeclaredFields());
        return Stream.of(
                Arguments.of(new TestObject(List.of("string"), Set.of(1)), fields, Collections.emptyList()),
                Arguments.of(new TestObject(null, null), fields, List.of("field1", "field2")),
                Arguments.of(new TestObject(null, Collections.emptyList()), fields, List.of("field1"))
        );
    }


    private record TestObject(@NotNull(message = "field1") Object field1,
                              @NotNull(message = "field2") Collection<?> field2) {
    }


}