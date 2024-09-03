package ru.clevertec.check.controller.console.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.controller.console.DefaultConsoleController;
import ru.clevertec.check.controller.console.args.ObjectMapper;
import ru.clevertec.check.controller.console.file.FileWriter;
import ru.clevertec.check.controller.console.view.CheckView;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.service.CheckService;
import ru.clevertec.check.service.ServiceException;
import ru.clevertec.check.util.ErrorCode;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultConsoleControllerImplTest {

    private DefaultConsoleController controller;
    @Mock
    private ObjectMapper<Order> mapper;
    @Mock
    private CheckService checkService;
    @Mock
    private FileWriter fileWriter;
    @Mock
    private CheckView checkView;

    @BeforeEach
    void setUp() {
        controller = new DefaultConsoleControllerImpl(mapper, checkService, fileWriter, checkView);
    }

    @Test
    void start_ifCheckServiceCompletedSuccessful() {
        when(checkView.getCsv(any(), anyString())).thenReturn("test");

        doAnswer(invocationOnMock -> {
            byte[] bytes = invocationOnMock.getArgument(1, byte[].class);
            assertArrayEquals("test".getBytes(), bytes);
            return null;
        }).when(fileWriter).write(anyString(), any());

        controller.start(new String[]{});
    }

    @Test
    void start_ifCheckServiceThrow() {
        when(checkService.getCheck(any())).thenThrow(new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR));

        doAnswer(invocationOnMock -> {
            byte[] bytes = invocationOnMock.getArgument(1, byte[].class);
            assertArrayEquals(("ERROR\n" + ErrorCode.INTERNAL_SERVER_ERROR.getTitle()).getBytes(), bytes);
            return null;
        }).when(fileWriter).write(anyString(), any());

        controller.start(new String[]{});
    }
}