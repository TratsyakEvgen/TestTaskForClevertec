package ru.clevertec.check.ioc.impl;

import ru.clevertec.check.ioc.ComponentDescription;
import ru.clevertec.check.ioc.Context;
import ru.clevertec.check.ioc.ContextBuilder;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;
import ru.clevertec.check.ioc.exception.ContextBuilderException;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.stream.Collectors;

public class ContextBuilderImpl implements ContextBuilder {
    @Override
    public Context init(Set<Class<?>> classSet) {
        Set<ComponentDescription> componentDescriptions = classSet.stream()
                .filter(clazz -> !clazz.isInterface())
                .filter(clazz -> clazz.isAnnotationPresent(NoSpringComponent.class))
                .map(clazz ->
                        {
                            try {
                                return ComponentDescription.builder()
                                        .qualifier(getQualifier(clazz))
                                        .types(clazz.getGenericInterfaces())
                                        .clazz(clazz)
                                        .object(clazz.getConstructor().newInstance())
                                        .build();
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                     NoSuchMethodException e) {
                                throw new ContextBuilderException("Can not creat instance " + clazz, e);
                            }
                        }
                )
                .collect(Collectors.toSet());

        return new ContextImpl(componentDescriptions);
    }

    private String getQualifier(Class<?> clazz) {
        String name = clazz.getName();
        return name.substring(name.lastIndexOf('.') + 1);
    }
}
