package ru.clevertec.check.controller.console.args.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.controller.console.args.ObjectDeserializer;
import ru.clevertec.check.model.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListProductsDeserializerTest {

    private ObjectDeserializer<List<Product>> objectDeserializer;


    @BeforeEach
    public void setup() {
        objectDeserializer = new ListProductsDeserializer();
    }

    @Test
    void deserializer_ifExistValues() {
        String[] args = new String[]{"3-1", "2-5", "discountCard=1111", "balanceDebitCard=100", "saveToFile=./result.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/test", "datasource.username=postgres", "datasource.password=dffd"};

        List<Product> productList = objectDeserializer.deserializer(args, "\\d+-\\d+", "-");
        assertEquals(List.of(
                        Product.builder().id(3L).quantity(1).build(),
                        Product.builder().id(2L).quantity(5).build()
                ),
                productList);

    }

    @Test
    void deserializer_ifNotExistValues() {
        String[] args = new String[]{"discountCard=1111", "balanceDebitCard=100", "saveToFile=./result.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/test", "datasource.username=postgres", "datasource.password=dffd"};

        List<Product> productList = objectDeserializer.deserializer(args, "\\d+-\\d+", "-");

        assertTrue(productList.isEmpty());
    }
}