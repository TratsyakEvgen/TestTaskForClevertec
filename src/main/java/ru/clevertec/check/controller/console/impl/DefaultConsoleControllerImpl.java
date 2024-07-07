package ru.clevertec.check.controller.console.impl;

import ru.clevertec.check.config.ApplicationConfig;
import ru.clevertec.check.controller.console.DefaultConsoleController;
import ru.clevertec.check.controller.console.args.ObjectMapper;
import ru.clevertec.check.controller.console.file.FileWriter;
import ru.clevertec.check.controller.console.view.CheckView;
import ru.clevertec.check.ioc.annotation.Inject;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;
import ru.clevertec.check.model.Check;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.service.CheckService;
import ru.clevertec.check.service.ServiceException;

@NoSpringComponent
public class DefaultConsoleControllerImpl implements DefaultConsoleController {
    @Inject
    private ObjectMapper<Order> mapper;
    @Inject
    private CheckService checkService;
    @Inject
    private FileWriter fileWriter;
    @Inject
    private CheckView checkView;

    public DefaultConsoleControllerImpl() {
    }

    public DefaultConsoleControllerImpl(ObjectMapper<Order> mapper,
                                        CheckService checkService,
                                        FileWriter fileWriter,
                                        CheckView checkView) {
        this.mapper = mapper;
        this.checkService = checkService;
        this.fileWriter = fileWriter;
        this.checkView = checkView;
    }

    @Override
    public void start(String[] args) {

        Order order = mapper.getObject(args);
        byte[] result;

        try {

            Check check = checkService.getCheck(order);
            result = checkView.getCsv(check, ApplicationConfig.CSV_SEPARATOR).getBytes();
            System.out.println(checkView.getString(check));

        } catch (ServiceException e) {

            String errorCode = e.getErrorCode().getTitle();
            System.out.println(errorCode);
            e.printStackTrace();
            result = ("ERROR\n" + errorCode).getBytes();

        }

        fileWriter.write(ApplicationConfig.RESULT, result);
    }
}
