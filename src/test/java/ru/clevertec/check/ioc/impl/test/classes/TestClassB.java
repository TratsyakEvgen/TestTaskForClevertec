package ru.clevertec.check.ioc.impl.test.classes;

import ru.clevertec.check.ioc.annotation.Inject;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;

@NoSpringComponent
public class TestClassB implements SomeInterfaceB {
    @Inject
    private SomeInterfaceA someInterfaceA;
    @Inject(qualifier = "TestClassC")
    private SomeInterfaceA someInterface;
}
