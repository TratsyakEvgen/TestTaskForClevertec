package ru.clevertec.check.service;

import ru.clevertec.check.model.Check;
import ru.clevertec.check.model.Order;

public interface CheckService {
    Check getCheck(Order order);
}
