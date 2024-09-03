package ru.clevertec.check.ioc.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.ioc.Context;
import ru.clevertec.check.ioc.ContextBuilder;
import ru.clevertec.check.ioc.exception.ContextBuilderException;
import ru.clevertec.check.ioc.impl.test.classes.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ContextBuilderImplTest {

    private ContextBuilder contextBuilder;

    @BeforeEach
    void setUp() {
        contextBuilder = new ContextBuilderImpl();
    }

    @Test
    void init_ifClassWithOutDeFaultConstructor() {
        assertThrows(ContextBuilderException.class,
                () -> contextBuilder.init(Set.of(TestClassWithOutDefaultConstructor.class)))
        ;
    }

    @Test
    void init_ifClassIsInterface() {
        Context context = contextBuilder.init(Set.of(Cloneable.class));

        assertTrue(context.getAllComponents().isEmpty());
    }

    @Test
    void init_ifClassNoComponent() {
        Context context = contextBuilder.init(Set.of(TestClassNoComponent.class));

        assertTrue(context.getAllComponents().isEmpty());
    }

    @Test
    void init_ifClassesAreComponent() {
        Context context = contextBuilder.init(Set.of(TestClassA.class, TestClassB.class, TestClassC.class));

        assertEquals(3, context.getAllComponents().size());
    }
}