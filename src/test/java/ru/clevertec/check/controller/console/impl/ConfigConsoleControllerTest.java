package ru.clevertec.check.controller.console.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.controller.console.DefaultConsoleController;
import ru.clevertec.check.controller.console.configurator.Configurator;
import ru.clevertec.check.controller.console.configurator.ConfiguratorException;
import ru.clevertec.check.controller.console.file.FileWriter;
import ru.clevertec.check.util.ErrorCode;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfigConsoleControllerTest {
    private DefaultConsoleController controller;
    @Mock
    private DefaultConsoleController defaultConsoleController;
    @Mock
    private FileWriter fileWriter;
    @Mock
    private Configurator configurator;

    @BeforeEach
    void setUp() {
        controller = new ConfigConsoleController(defaultConsoleController, fileWriter, configurator);
    }

    @Test
    void start_ifConfiguratorThrow() {
        doThrow(ConfiguratorException.class).when(configurator).tune(any());

        doAnswer(invocationOnMock -> {
            byte[] bytes = invocationOnMock.getArgument(1, byte[].class);
            assertArrayEquals(("ERROR\n" + ErrorCode.BAD_REQUEST.getTitle()).getBytes(), bytes);
            return null;
        }).when(fileWriter).write(anyString(), any());

        controller.start(new String[]{});
    }
}