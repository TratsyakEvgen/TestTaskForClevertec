package ru.clevertec.check.controller.console.view.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.controller.console.view.CheckView;
import ru.clevertec.check.model.Check;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CheckViewImplTest {

    private CheckView checkView;

    private Check check;

    @BeforeEach
    void setUp() {
        checkView = new CheckViewImpl();
        check = Check.builder()
                .localDateTime(LocalDateTime.of(2024, 10, 1, 0, 0))
                .price(List.of(
                        Check.Price.builder()
                                .total(BigDecimal.TEN)
                                .discount(BigDecimal.ONE)
                                .build()
                ))
                .totalDiscount(BigDecimal.ZERO)
                .totalPrice(BigDecimal.ONE)
                .totalWithDiscount(BigDecimal.ZERO)
                .products(List.of(Product.builder()
                        .id(1L)
                        .price(BigDecimal.valueOf(10))
                        .wholesaleProduct(true)
                        .quantity(10)
                        .description("some text")
                        .build()))
                .discountCard(DiscountCard.builder()
                        .id(2L)
                        .amount((short) 2)
                        .build())
                .build();
    }

    @Test
    void getCsv() {
        String expected = """
                Date;Time
                01.10.2024;00:00:00

                QTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL
                10;some text;10$;1$;10$

                DISCOUNT CARD;DISCOUNT PERCENTAGE
                0000;2%

                TOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT
                1$;0$;0$""";
        String actual = checkView.getCsv(check, ";");

        assertEquals(expected, actual);

    }

    @Test
    void getString() {
        String expected = """
                Date Time
                01.10.2024 00:00:00
                                
                QTY DESCRIPTION PRICE DISCOUNT TOTAL
                10 some text 10$ 1$ 10$
                                
                DISCOUNT CARD DISCOUNT PERCENTAGE
                0000 2%
                                
                TOTAL PRICE TOTAL DISCOUNT TOTAL WITH DISCOUNT
                1$ 0$ 0$""";
        String actual = checkView.getString(check);

        assertEquals(expected, actual);
    }
}