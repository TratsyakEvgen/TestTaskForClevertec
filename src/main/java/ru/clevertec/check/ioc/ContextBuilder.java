package ru.clevertec.check.ioc;

import java.util.Set;

public interface ContextBuilder {
    Context init(Set<Class<?>> classSet);

}
