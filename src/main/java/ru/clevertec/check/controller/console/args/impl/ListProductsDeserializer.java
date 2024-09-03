package ru.clevertec.check.controller.console.args.impl;

import ru.clevertec.check.controller.console.args.ObjectDeserializer;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;
import ru.clevertec.check.model.Product;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@NoSpringComponent
public class ListProductsDeserializer implements ObjectDeserializer<List<Product>> {

    @Override
    public List<Product> deserializer(String[] strings, String regEx, String split) {
        return Arrays.stream(strings)
                .filter(s -> s.matches(regEx))
                .map(s -> {
                    String[] splitString = s.split(split);
                    return Product.builder()
                            .id(Long.parseLong(splitString[0]))
                            .quantity(Integer.parseInt(splitString[1]))
                            .build();
                })
                .collect(Collectors.toList());
    }
}
