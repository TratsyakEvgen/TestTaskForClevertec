package ru.clevertec.check.service.validator.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.service.validator.AnnotationExecutor;
import ru.clevertec.check.service.validator.FieldScanner;
import ru.clevertec.check.service.validator.annotation.NotEmpty;
import ru.clevertec.check.service.validator.exception.ValidatorException;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotEmptyExecutorTest {

    private AnnotationExecutor<NotEmpty> annotationExecutor;
    @Mock
    private FieldScanner fieldScanner;

    @BeforeEach
    void setup() {
        annotationExecutor = new NotEmptyExecutor(fieldScanner);
    }

    @ParameterizedTest
    @MethodSource("generateTestObjectAndListFieldAndExpectedMessages")
    void execute(TestObject testObject, List<Field> fields, List<String> expected) {
        List<String> actual = new ArrayList<>();
        when(fieldScanner.findAnnotation(NotEmpty.class, testObject)).thenReturn(fields);

        annotationExecutor.execute(testObject, actual);

        assertEquals(expected, actual);
    }

    @Test
    void execute_incorrectField() {
        TestObjectWithIncorrectField testObject = new TestObjectWithIncorrectField("");
        when(fieldScanner.findAnnotation(NotEmpty.class, testObject))
                .thenReturn(List.of(TestObjectWithIncorrectField.class.getDeclaredFields()));

        assertThrows(ValidatorException.class, () -> annotationExecutor.execute(testObject, Collections.emptyList()));
    }

    static Stream<Arguments> generateTestObjectAndListFieldAndExpectedMessages() {
        Class<?> testObjectClass = TestObject.class;
        List<Field> fields = List.of(testObjectClass.getDeclaredFields());
        return Stream.of(
                Arguments.of(new TestObject(List.of("string"), Set.of(1)), fields, Collections.emptyList()),
                Arguments.of(new TestObject(Collections.emptyList(), Collections.emptySet()), fields, List.of("field1", "field2")),
                Arguments.of(new TestObject(List.of("string"), Collections.emptyList()), fields, List.of("field2"))
        );
    }


    private record TestObject(@NotEmpty(message = "field1") Collection<?> field1,
                              @NotEmpty(message = "field2") Collection<?> field2) {
    }

    private record TestObjectWithIncorrectField(@NotEmpty(message = "field1") String field1) {
    }

}