package ru.clevertec.check.controller.console.args.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.controller.console.args.ObjectDeserializer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringDeserializerTest {

    private ObjectDeserializer<String> objectDeserializer;


    @BeforeEach
    public void init() {
        objectDeserializer = new StringDeserializer();
    }

    @Test
    void deserializer_ifExistValue() {
        String[] args = new String[]{"3-1", "2-5", "discountCard=1111", "balanceDebitCard=100", "saveToFile=./result.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/test", "datasource.username=postgres", "datasource.password=dffd"};

        String string = objectDeserializer.deserializer(args, "discountCard=\\d{4}", "=");
        assertEquals("1111", string);

    }

    @Test
    void deserializer_ifNotExistValue() {
        String[] args = new String[]{"saveToFile=./result.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/test", "datasource.username=postgres", "datasource.password=dffd"};

        String string = objectDeserializer.deserializer(args, "discountCard=\\d{4}", "=");

        assertEquals("", string);

    }

}