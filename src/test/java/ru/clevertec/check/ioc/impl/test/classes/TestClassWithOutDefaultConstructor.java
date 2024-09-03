package ru.clevertec.check.ioc.impl.test.classes;

import ru.clevertec.check.ioc.annotation.NoSpringComponent;

@NoSpringComponent
public class TestClassWithOutDefaultConstructor implements SomeInterfaceA {

    private final SomeInterfaceA someInterfaceA;

    public TestClassWithOutDefaultConstructor(SomeInterfaceA someInterfaceA) {
        this.someInterfaceA = someInterfaceA;
    }
}
