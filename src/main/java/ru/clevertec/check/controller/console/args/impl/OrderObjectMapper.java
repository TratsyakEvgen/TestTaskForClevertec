package ru.clevertec.check.controller.console.args.impl;


import ru.clevertec.check.controller.console.args.ObjectDeserializer;
import ru.clevertec.check.controller.console.args.ObjectMapper;
import ru.clevertec.check.ioc.annotation.Inject;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.Product;

import java.math.BigDecimal;
import java.util.List;

@NoSpringComponent
public class OrderObjectMapper implements ObjectMapper<Order> {
    @Inject
    private ObjectDeserializer<List<Product>> listProductsDeserializer;

    @Inject
    private ObjectDeserializer<String> stringDeserializer;

    @Inject
    private ObjectDeserializer<BigDecimal> bigDecimalDeserializer;


    public OrderObjectMapper() {
    }

    public OrderObjectMapper(ObjectDeserializer<List<Product>> listProductsDeserializer,
                             ObjectDeserializer<String> stringDeserializer,
                             ObjectDeserializer<BigDecimal> bigDecimalDeserializer) {
        this.listProductsDeserializer = listProductsDeserializer;
        this.stringDeserializer = stringDeserializer;
        this.bigDecimalDeserializer = bigDecimalDeserializer;
    }


    @Override
    public Order getObject(String[] arg) {
        return Order.builder()
                .products(listProductsDeserializer.deserializer(arg, "\\d+-\\d+", "-"))
                .discountCard(stringDeserializer.deserializer(arg, "discountCard=\\d{4}", "="))
                .balanceDebitCard(bigDecimalDeserializer.deserializer(arg, "balanceDebitCard=-?\\d+(\\.?\\d{2}){0,2}", "="))
                .build();
    }
}
