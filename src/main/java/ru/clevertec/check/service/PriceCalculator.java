package ru.clevertec.check.service;

import ru.clevertec.check.model.Check;

public interface PriceCalculator {
    void calculateDiscount(Check check);

    void calculateTotalPrice(Check check);
}
