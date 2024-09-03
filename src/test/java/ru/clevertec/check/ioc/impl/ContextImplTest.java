package ru.clevertec.check.ioc.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.ioc.ComponentDescription;
import ru.clevertec.check.ioc.Context;
import ru.clevertec.check.ioc.exception.ContextException;
import ru.clevertec.check.ioc.impl.test.classes.TestClassA;
import ru.clevertec.check.ioc.impl.test.classes.TestClassB;
import ru.clevertec.check.ioc.impl.test.classes.TestClassC;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ContextImplTest {

    private Context context;

    @BeforeEach
    void setUp() {
        ComponentDescription componentDescriptionA =
                ComponentDescription.builder()
                        .object(new TestClassA())
                        .qualifier("TestClassA")
                        .clazz(TestClassA.class)
                        .types(TestClassA.class.getGenericInterfaces())
                        .build();
        ComponentDescription componentDescriptionB =
                ComponentDescription.builder()
                        .object(new TestClassB())
                        .qualifier("TestClassB")
                        .clazz(TestClassB.class)
                        .types(TestClassB.class.getGenericInterfaces())
                        .build();
        ComponentDescription componentDescriptionC =
                ComponentDescription.builder()
                        .object(new TestClassC())
                        .qualifier("TestClassC")
                        .clazz(TestClassC.class)
                        .types(TestClassC.class.getGenericInterfaces())
                        .build();

        context = new ContextImpl(Set.of(componentDescriptionA, componentDescriptionB, componentDescriptionC));
    }

    @Test
    void getAllComponents() {
        assertEquals(3, context.getAllComponents().size());
    }

    @Test
    void getComponent_byQualifier() {
        Object object = context.getComponent("TestClassC");

        assertEquals(TestClassC.class, object.getClass());
    }

    @Test
    void getComponent_byType() {
        assertThrows(ContextException.class, () -> context.getComponent(TestClassA.class.getGenericInterfaces()[0]));
    }

    @Test
    void getComponent_ByClass() {
        Object object = context.getComponent(TestClassB.class);

        assertEquals(TestClassB.class, object.getClass());
    }

    @Test
    void checkByType() {
        assertTrue(context.checkByType(TestClassA.class.getGenericInterfaces()[0]));
    }
}