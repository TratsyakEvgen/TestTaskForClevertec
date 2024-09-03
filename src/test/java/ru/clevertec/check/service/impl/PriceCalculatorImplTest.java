package ru.clevertec.check.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.clevertec.check.model.Check;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.service.PriceCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PriceCalculatorImplTest {

    private PriceCalculator priceCalculator;

    @BeforeEach
    public void setup() {
        priceCalculator = new PriceCalculatorImpl(BigDecimal.valueOf(0.1), 5);
    }


    @ParameterizedTest
    @MethodSource("generateCheckAndExpectedValue")
    void calculateDiscount(Check check, BigDecimal expected) {
        priceCalculator.calculateDiscount(check);

        assertEquals(expected, check.getPrice().getFirst().getDiscount());
    }


    @Test
    public void calculateTotal_CheckTotal() {
        Check check = Check.builder()
                .price(List.of(
                        Check.Price.builder()
                                .total(BigDecimal.TEN)
                                .discount(BigDecimal.ONE)
                                .build(),
                        Check.Price.builder()
                                .total(BigDecimal.TEN)
                                .discount(BigDecimal.ZERO)
                                .build()
                ))
                .build();

        priceCalculator.calculateTotalPrice(check);

        assertEquals(BigDecimal.valueOf(20), check.getTotalPrice());
        assertEquals(BigDecimal.ONE, check.getTotalDiscount());
        assertEquals(BigDecimal.valueOf(19), check.getTotalWithDiscount());
    }

    static Stream<Arguments> generateCheckAndExpectedValue() {
        return Stream.of(
                Arguments.of(
                        Check.builder()
                                .products(List.of(Product.builder()
                                        .price(BigDecimal.valueOf(10))
                                        .wholesaleProduct(true)
                                        .quantity(10)
                                        .build()))
                                .build(),
                        BigDecimal.TEN.setScale(2, RoundingMode.HALF_UP)
                ),

                Arguments.of(
                        Check.builder()
                                .products(List.of(Product.builder()
                                        .price(BigDecimal.valueOf(10))
                                        .wholesaleProduct(true)
                                        .quantity(10)
                                        .build()))
                                .build(),
                        BigDecimal.TEN.setScale(2, RoundingMode.HALF_UP)
                ),

                Arguments.of(
                        Check.builder()
                                .products(List.of(Product.builder()
                                        .price(BigDecimal.valueOf(10))
                                        .wholesaleProduct(false)
                                        .quantity(10)
                                        .build()))
                                .build(),
                        BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
                ),

                Arguments.of(
                        Check.builder()
                                .products(List.of(Product.builder()
                                        .price(BigDecimal.valueOf(10))
                                        .wholesaleProduct(true)
                                        .quantity(10)
                                        .build()))
                                .discountCard(DiscountCard.builder()
                                        .amount((short) 2)
                                        .build())
                                .build(),
                        BigDecimal.TEN.setScale(2, RoundingMode.HALF_UP)
                ),

                Arguments.of(
                        Check.builder()
                                .products(List.of(Product.builder()
                                        .price(BigDecimal.valueOf(10))
                                        .wholesaleProduct(false)
                                        .quantity(10)
                                        .build()))
                                .discountCard(DiscountCard.builder()
                                        .amount((short) 2)
                                        .build())
                                .build(),
                        BigDecimal.TWO.setScale(2, RoundingMode.HALF_UP)
                ),

                Arguments.of(
                        Check.builder()
                                .products(List.of(
                                        Product.builder()
                                                .price(BigDecimal.valueOf(10))
                                                .wholesaleProduct(true)
                                                .quantity(1)
                                                .build(),

                                        Product.builder()
                                                .price(BigDecimal.valueOf(10))
                                                .wholesaleProduct(true)
                                                .quantity(4)
                                                .build()

                                ))
                                .build(),
                        BigDecimal.ONE.setScale(2, RoundingMode.HALF_UP)
                )


        );
    }


}