package ru.clevertec.check.controller.console.args.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.controller.console.args.ObjectDeserializer;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class BigDecimalDeserializerTest {
    private ObjectDeserializer<BigDecimal> objectDeserializer;


    @BeforeEach
    public void setup() {
        objectDeserializer = new BigDecimalDeserializer();
    }

    @Test
    void deserializer_ifExistValue() {
        String[] args = new String[]{"3-1", "2-5", "discountCard=1111", "balanceDebitCard=100", "saveToFile=./result.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/test", "datasource.username=postgres", "datasource.password=dffd"};

        BigDecimal actual = objectDeserializer.deserializer(args, "balanceDebitCard=-?\\d+(\\.\\d{1,2})?", "=");
        assertEquals(BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_UP), actual);

    }

    @Test
    void deserializer_ifNotExistValue() {
        String[] args = new String[]{"discountCard=1111", "saveToFile=./result.csv",
                "datasource.url=jdbc:postgresql://localhost:5432/test", "datasource.username=postgres", "datasource.password=dffd"};

        BigDecimal bigDecimal = objectDeserializer.deserializer(args, "\\d+-\\d+", "-");

        assertNull(bigDecimal);

    }

}