package ru.clevertec.check.controller.console.view;

import ru.clevertec.check.model.Check;

public interface CheckView {
    String getCsv(Check check, String separator);

    String getString(Check check);
}
