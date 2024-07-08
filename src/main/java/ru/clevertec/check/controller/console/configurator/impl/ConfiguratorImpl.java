package ru.clevertec.check.controller.console.configurator.impl;

import ru.clevertec.check.config.ApplicationConfig;
import ru.clevertec.check.controller.console.args.ObjectDeserializer;
import ru.clevertec.check.controller.console.configurator.Configurator;
import ru.clevertec.check.controller.console.configurator.ConfiguratorException;
import ru.clevertec.check.ioc.annotation.Inject;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;

@NoSpringComponent
public class ConfiguratorImpl implements Configurator {
    @Inject
    private ObjectDeserializer<String> objectDeserializer;

    public ConfiguratorImpl() {
    }

    public ConfiguratorImpl(ObjectDeserializer<String> objectDeserializer) {
        this.objectDeserializer = objectDeserializer;
    }

    @Override
    public void tune(String[] args) {
        String saveToFile = objectDeserializer.deserializer(args, "saveToFile=.+", "=");
        String pathToFile = objectDeserializer.deserializer(args, "pathToFile=.+", "=");

        if (saveToFile.isEmpty()) {
            throw new ConfiguratorException("No parameter (saveToFile) passed");
        }

        ApplicationConfig.RESULT = saveToFile;

        if (pathToFile.isEmpty()) {
            throw new ConfiguratorException("No parameter (pathToFile) passed");
        }

        ApplicationConfig.PRODUCTS = pathToFile;

    }
}
