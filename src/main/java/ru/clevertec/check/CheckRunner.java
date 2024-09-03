package ru.clevertec.check;


import ru.clevertec.check.controller.console.DefaultConsoleController;
import ru.clevertec.check.controller.console.impl.DBConsoleController;
import ru.clevertec.check.ioc.Application;
import ru.clevertec.check.ioc.Context;


public class CheckRunner {

    public static void main(String[] args) {
        Context context = Application.run();
        DefaultConsoleController controller = (DefaultConsoleController) context.getComponent(DBConsoleController.class);
        controller.start(args);
    }

}
