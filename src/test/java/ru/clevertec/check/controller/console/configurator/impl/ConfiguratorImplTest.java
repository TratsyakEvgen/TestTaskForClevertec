package ru.clevertec.check.controller.console.configurator.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.controller.console.args.ObjectDeserializer;
import ru.clevertec.check.controller.console.configurator.Configurator;
import ru.clevertec.check.controller.console.configurator.ConfiguratorException;
import ru.clevertec.check.repository.connection.pool.ConnectionPool;
import ru.clevertec.check.repository.connection.pool.ConnectionPoolException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfiguratorImplTest {

    private Configurator configurator;
    @Mock
    private ConnectionPool connectionPool;
    @Mock
    private ObjectDeserializer<String> objectDeserializer;

    @BeforeEach
    void setUp() {
        configurator = new ConfiguratorImpl(objectDeserializer, connectionPool);
    }

    @Test
    void tune_ifNotFoundParam() {
        when(objectDeserializer.deserializer(any(), anyString(), anyString())).thenReturn("");

        assertThrows(ConfiguratorException.class, () -> configurator.tune(new String[]{}));
    }

    @Test
    void tune_ifConnectionPollThrow() {
        when(objectDeserializer.deserializer(any(), anyString(), anyString())).thenReturn("some string");
        doThrow(ConnectionPoolException.class).when(connectionPool).initPoolData(any());

        assertThrows(ConfiguratorException.class, () -> configurator.tune(new String[]{}));
    }
}