package ru.clevertec.check.controller.console.configurator.impl;

import ru.clevertec.check.config.ApplicationConfig;
import ru.clevertec.check.controller.console.args.ObjectDeserializer;
import ru.clevertec.check.controller.console.configurator.Configurator;
import ru.clevertec.check.controller.console.configurator.ConfiguratorException;
import ru.clevertec.check.ioc.annotation.Inject;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;
import ru.clevertec.check.repository.connection.pool.ConnectionPool;
import ru.clevertec.check.repository.connection.pool.ConnectionPoolException;
import ru.clevertec.check.repository.connection.pool.PoolData;

@NoSpringComponent
public class ConfiguratorImpl implements Configurator {
    @Inject
    private ObjectDeserializer<String> objectDeserializer;
    @Inject
    private ConnectionPool connectionPool;

    public ConfiguratorImpl() {
    }


    public ConfiguratorImpl(ObjectDeserializer<String> objectDeserializer, ConnectionPool connectionPool) {
        this.objectDeserializer = objectDeserializer;
        this.connectionPool = connectionPool;
    }

    @Override
    public void tune(String[] args) {
        ApplicationConfig.RESULT = getParam("saveToFile", args);

        String url = getParam("datasource.url", args);
        String userName = getParam("datasource.username", args);
        String password = getParam("datasource.password", args);

        PoolData poolData = PoolData.builder().url(url).user(userName).password(password).poolSize(1).build();

        try {
            connectionPool.initPoolData(poolData);
        } catch (ConnectionPoolException e) {
            throw new ConfiguratorException("Exception configuration connection pool ", e);
        }

    }

    private String getParam(String name, String[] args) {
        String param = objectDeserializer.deserializer(args, name + "=.+", "=");

        if (param.isEmpty()) {
            throw new ConfiguratorException(String.format("No parameter (%s) passed", name));
        }

        return param;
    }
}
