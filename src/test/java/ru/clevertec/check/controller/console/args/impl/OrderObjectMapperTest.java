package ru.clevertec.check.controller.console.args.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.controller.console.args.ObjectMapper;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.Product;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderObjectMapperTest {

    private ObjectMapper<Order> objectMapper;

    @BeforeEach
    public void init() {
        objectMapper = new OrderObjectMapper(new ListProductsDeserializer(),
                new StringDeserializer(),
                new BigDecimalDeserializer());
    }

    @Test
    void deserializer_ifExistValues() {
        String[] args = new String[]{"3-1", "discountCard=1111", "balanceDebitCard=100", "saveToFile=./result.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/test", "datasource.username=postgres", "datasource.password=dffd"};

        Order order = objectMapper.getObject(args);
        assertEquals(Order.builder()
                        .products(List.of(Product.builder().id(3L).quantity(1).build()))
                        .balanceDebitCard(BigDecimal.valueOf(100))
                        .discountCard("1111")
                        .build(),
                order
        );
    }

    @Test
    void deserializer_ifNotExistValues() {
        String[] args = new String[]{"saveToFile=./result.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/test", "datasource.username=postgres", "datasource.password=dffd"};

        Order order = objectMapper.getObject(args);

        assertEquals(Order.builder().products(Collections.emptyList()).discountCard("").build(), order);

    }

}