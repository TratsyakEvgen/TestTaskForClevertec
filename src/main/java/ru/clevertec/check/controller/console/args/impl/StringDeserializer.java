package ru.clevertec.check.controller.console.args.impl;

import ru.clevertec.check.controller.console.args.ObjectDeserializer;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;

import java.util.Arrays;

@NoSpringComponent
public class StringDeserializer implements ObjectDeserializer<String> {

    @Override
    public String deserializer(String[] strings, String regEx, String split) {
        return Arrays.stream(strings)
                .filter(p -> p.matches(regEx))
                .findFirst()
                .map(d -> {
                    String[] splitString = d.split(split);
                    return splitString[1];
                })
                .orElse("");
    }
}
