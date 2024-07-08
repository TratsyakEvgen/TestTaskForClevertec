package ru.clevertec.check.controller.console.impl;

import ru.clevertec.check.config.ApplicationConfig;
import ru.clevertec.check.controller.console.DefaultConsoleController;
import ru.clevertec.check.controller.console.configurator.Configurator;
import ru.clevertec.check.controller.console.configurator.ConfiguratorException;
import ru.clevertec.check.controller.console.file.FileWriter;
import ru.clevertec.check.ioc.annotation.Inject;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;
import ru.clevertec.check.util.ErrorCode;

@NoSpringComponent
public class ConfigConsoleController implements DefaultConsoleController {
    @Inject(qualifier = "DefaultConsoleControllerImpl")
    private DefaultConsoleController defaultConsoleController;

    @Inject
    private FileWriter fileWriter;

    @Inject
    private Configurator configurator;

    public ConfigConsoleController() {
    }

    public ConfigConsoleController(DefaultConsoleController defaultConsoleController,
                                   FileWriter fileWriter,
                                   Configurator configurator) {
        this.defaultConsoleController = defaultConsoleController;
        this.fileWriter = fileWriter;
        this.configurator = configurator;
    }

    @Override
    public void start(String[] args) {

        try {
            configurator.tune(args);
        } catch (ConfiguratorException e) {
            String errorCode = ErrorCode.BAD_REQUEST.getTitle();
            System.out.println(errorCode);
            e.printStackTrace();
            fileWriter.write(ApplicationConfig.RESULT, ("ERROR\n" + errorCode).getBytes());
            return;
        }

        defaultConsoleController.start(args);
    }
}
