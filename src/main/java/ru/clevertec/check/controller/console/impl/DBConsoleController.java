package ru.clevertec.check.controller.console.impl;

import ru.clevertec.check.controller.console.DefaultConsoleController;
import ru.clevertec.check.ioc.annotation.Inject;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;
import ru.clevertec.check.repository.connection.pool.ConnectionPool;
import ru.clevertec.check.repository.connection.pool.ConnectionPoolException;

@NoSpringComponent
public class DBConsoleController implements DefaultConsoleController {

    @Inject(qualifier = "ConfigConsoleController")
    private DefaultConsoleController defaultConsoleController;
    @Inject
    private ConnectionPool connectionPool;

    public DBConsoleController() {
    }

    public DBConsoleController(DefaultConsoleController defaultConsoleController) {
        this.defaultConsoleController = defaultConsoleController;
    }

    @Override
    public void start(String[] args) {
        defaultConsoleController.start(args);
        try {
            connectionPool.destroy();
        } catch (ConnectionPoolException e) {
            e.printStackTrace();
        }
    }
}
