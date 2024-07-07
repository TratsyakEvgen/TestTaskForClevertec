package ru.clevertec.check.ioc;

import java.lang.reflect.Type;
import java.util.Set;

public interface Context {

    Set<Object> getAllComponents();

    Object getComponent(String qualifier);

    Object getComponent(Type type);

    Object getComponent(Class<?> aClass);

    boolean checkByType(Type type);
}
