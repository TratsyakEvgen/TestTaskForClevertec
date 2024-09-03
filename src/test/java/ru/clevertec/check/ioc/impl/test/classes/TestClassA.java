package ru.clevertec.check.ioc.impl.test.classes;

import ru.clevertec.check.ioc.annotation.Inject;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;

@NoSpringComponent
public class TestClassA implements SomeInterfaceA {
    @Inject
    private SomeInterfaceB someInterfaceB;
}
