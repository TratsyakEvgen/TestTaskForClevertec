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
import ru.clevertec.check.service.validator.annotation.Pattern;
import ru.clevertec.check.service.validator.exception.ValidatorException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PatternExecutorTest {
    private AnnotationExecutor<Pattern> annotationExecutor;
    @Mock
    private FieldScanner fieldScanner;

    @BeforeEach
    public void setup() {
        annotationExecutor = new PatternExecutor(fieldScanner);
    }


    @ParameterizedTest
    @MethodSource("generateTestObjectAndListFieldAndExpectedMessages")
    void execute(TestObject testObject, List<Field> fields, List<String> expected) {
        List<String> actual = new ArrayList<>();
        Mockito.when(fieldScanner.findAnnotation(Pattern.class, testObject)).thenReturn(fields);

        annotationExecutor.execute(testObject, actual);

        assertEquals(expected, actual);
    }

    @Test
    public void execute_incorrectField() {
        TestObjectWithIncorrectField testObject = new TestObjectWithIncorrectField(1);
        Mockito.when(fieldScanner.findAnnotation(Pattern.class, testObject))
                .thenReturn(List.of(TestObjectWithIncorrectField.class.getDeclaredFields()));

        assertThrows(ValidatorException.class, () -> annotationExecutor.execute(testObject, Collections.emptyList()));
    }

    static Stream<Arguments> generateTestObjectAndListFieldAndExpectedMessages() {
        Class<?> testObjectClass = TestObject.class;
        List<Field> fields = List.of(testObjectClass.getDeclaredFields());
        return Stream.of(
                Arguments.of(new TestObject("field1", "field2"), fields, Collections.emptyList()),
                Arguments.of(new TestObject("", ""), fields, List.of("field1", "field2")),
                Arguments.of(new TestObject(null, "field2"), fields, List.of("field1"))
        );
    }


    private record TestObject(@Pattern(regexp = ".+", message = "field1") String field1,
                              @Pattern(regexp = ".+", message = "field2") String field2) {
    }

    private record TestObjectWithIncorrectField(@Pattern(regexp = ".+", message = "field1") int field1) {
    }

}