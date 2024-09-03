package ru.clevertec.check.ioc.impl;

import ru.clevertec.check.ioc.ComponentDescription;
import ru.clevertec.check.ioc.Context;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;
import ru.clevertec.check.ioc.exception.ContextException;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoSpringComponent
public class ContextImpl implements Context {

    private Set<ComponentDescription> componentDescriptions;

    public ContextImpl() {
    }

    public ContextImpl(Set<ComponentDescription> componentDescriptions) {
        this.componentDescriptions = componentDescriptions;
    }

    @Override
    public Set<Object> getAllComponents() {
        return componentDescriptions.stream().map(ComponentDescription::object).collect(Collectors.toSet());
    }

    @Override
    public Object getComponent(String qualifier) {

        List<ComponentDescription> descriptions = componentDescriptions.stream()
                .filter(componentDescription -> componentDescription.qualifier().equals(qualifier))
                .toList();

        return checkCount(descriptions, "qualifier " + qualifier);

    }

    @Override
    public Object getComponent(Type type) {

        List<ComponentDescription> descriptions = getListByType(type);

        return checkCount(descriptions, "type " + type);

    }


    @Override
    public Object getComponent(Class<?> clazz) {

        List<ComponentDescription> descriptions = componentDescriptions.stream()
                .filter(componentDescription -> componentDescription.clazz().equals(clazz))
                .toList();

        return checkCount(descriptions, "class " + clazz);

    }

    @Override
    public boolean checkByType(Type type) {
        return !getListByType(type).isEmpty();
    }


    private List<ComponentDescription> getListByType(Type type) {
        return componentDescriptions.stream()
                .filter(componentDescription -> {
                    long count = Arrays.stream(componentDescription.types())
                            .filter(t -> t.equals(type))
                            .count();
                    return count == 1;
                })
                .toList();
    }


    private Object checkCount(List<ComponentDescription> descriptions, String exceptionMessage) {

        int size = descriptions.size();

        if (size > 1) {
            throw new ContextException(String.format("Found %d components with %s", size, exceptionMessage));
        }

        if (size == 0) {
            throw new ContextException("Not found components with " + exceptionMessage);
        }

        return descriptions.getFirst().object();

    }
}
