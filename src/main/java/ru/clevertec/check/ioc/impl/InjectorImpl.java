package ru.clevertec.check.ioc.impl;

import ru.clevertec.check.ioc.Context;
import ru.clevertec.check.ioc.Injector;
import ru.clevertec.check.ioc.annotation.Inject;
import ru.clevertec.check.ioc.exception.ContextException;
import ru.clevertec.check.ioc.exception.InjectorException;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;


public class InjectorImpl implements Injector {
    @Override
    public void inject(Context context) {
        for (Object object : context.getAllComponents()) {
            Arrays.stream(object.getClass().getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Inject.class))
                    .forEach(field -> {

                        Inject inject = field.getAnnotation(Inject.class);
                        String qualifier = inject.qualifier();

                        if (!qualifier.isEmpty()) {
                            injectComponentByQualifier(context, field, object, qualifier);
                            return;
                        }

                        Type type = field.getGenericType();
                        if (context.checkByType(type)) {
                            injectComponentByType(context, field, object, type);
                            return;
                        }

                        injectComponentByClass(context, field, object);
                    });
        }

    }

    private void injectComponentByQualifier(Context context, Field field, Object object, String qualifier) {
        try {
            Object value = context.getComponent(qualifier);
            setField(field, object, value);
        } catch (ContextException e) {
            throw new InjectorException(String.format("Can not inject field %s of object %s by qualifier %s", field, object, qualifier), e);
        }
    }

    private void injectComponentByType(Context context, Field field, Object object, Type type) {
        try {
            Object value = context.getComponent(type);
            setField(field, object, value);
        } catch (ContextException e) {
            throw new InjectorException(String.format("Can not inject field %s of object %s by type %s", field, object, type), e);
        }
    }


    private void injectComponentByClass(Context context, Field field, Object object) {

        Class<?> clazz = field.getType();
        try {
            Object value = context.getComponent(clazz);
            setField(field, object, value);
        } catch (ContextException e) {
            throw new InjectorException(String.format("Can not get component for field %s of object %s by class %s", field, object, clazz), e);
        }

    }


    private void setField(Field field, Object object, Object value) {
        field.setAccessible(true);
        try {
            field.set(object, value);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new InjectorException(String.format("Can not inject value %s in field %s of object %s", value, field, object), e);
        }
    }


}
