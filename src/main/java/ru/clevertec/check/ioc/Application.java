package ru.clevertec.check.ioc;

import ru.clevertec.check.ioc.impl.ClassScannerImpl;
import ru.clevertec.check.ioc.impl.ContextBuilderImpl;
import ru.clevertec.check.ioc.impl.InjectorImpl;

import java.util.Set;

public class Application {
    public static Context run() {
        Set<Class<?>> classSet = new ClassScannerImpl().findAllClasses();
        Context context = new ContextBuilderImpl().init(classSet);
        new InjectorImpl().inject(context);
        return context;
    }
}
