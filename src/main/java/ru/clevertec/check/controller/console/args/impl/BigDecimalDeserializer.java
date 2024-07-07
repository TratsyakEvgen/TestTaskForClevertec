package ru.clevertec.check.controller.console.args.impl;


import ru.clevertec.check.controller.console.args.ObjectDeserializer;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

@NoSpringComponent
public class BigDecimalDeserializer implements ObjectDeserializer<BigDecimal> {

    @Override
    public BigDecimal deserializer(String[] strings, String regEx, String split) {
        return Arrays.stream(strings)
                .filter(p -> p.matches(regEx))
                .findFirst()
                .map(d -> {
                    String[] splitString = d.split(split);
                    return new BigDecimal(splitString[1]).setScale(2, RoundingMode.HALF_UP);
                })
                .orElse(null);
    }
}
