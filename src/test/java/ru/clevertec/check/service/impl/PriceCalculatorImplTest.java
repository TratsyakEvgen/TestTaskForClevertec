package ru.clevertec.check.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.model.Check;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.service.PriceCalculator;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PriceCalculatorImplTest {

    private PriceCalculator priceCalculator;

    @BeforeEach
    public void init() {
        priceCalculator = new PriceCalculatorImpl(BigDecimal.valueOf(0.1), 5);
    }

    @Test
    public void calculateDiscount_TotalPrice() {
        Check check = Check.builder()
                .products(List.of(Product.builder()
                        .id(1)
                        .price(BigDecimal.valueOf(10))
                        .wholesaleProduct(true)
                        .description("test")
                        .quantity(10)
                        .build()))
                .build();

        priceCalculator.calculateDiscount(check);

        assertEquals(0, check.getPrice().getFirst().getDiscount().compareTo(BigDecimal.valueOf(10)));
    }

    @Test
    public void calculateDiscount_ifNoDiscountCardAndWholesaleProduct() {
        Check check = Check.builder()
                .products(List.of(Product.builder()
                        .id(1)
                        .price(BigDecimal.valueOf(10))
                        .wholesaleProduct(true)
                        .description("test")
                        .quantity(10)
                        .build()))
                .build();

        priceCalculator.calculateDiscount(check);

        assertEquals(0, check.getPrice().getFirst().getDiscount().compareTo(BigDecimal.TEN));
    }

    @Test
    public void calculateDiscount_ifNoDiscountCardAndNoWholesaleProduct() {
        Check check = Check.builder()
                .products(List.of(Product.builder()
                        .id(1)
                        .price(BigDecimal.valueOf(10))
                        .wholesaleProduct(false)
                        .description("test")
                        .quantity(10)
                        .build()))
                .build();

        priceCalculator.calculateDiscount(check);

        assertEquals(0, check.getPrice().getFirst().getDiscount().compareTo(BigDecimal.ZERO));
    }

    @Test
    public void calculateDiscount_ifDiscountCardAndWholesaleProduct() {
        Check check = Check.builder()
                .products(List.of(Product.builder()
                        .id(1)
                        .price(BigDecimal.valueOf(10))
                        .wholesaleProduct(true)
                        .description("test")
                        .quantity(10)
                        .build()))
                .discountCard(DiscountCard.builder()
                        .amount((short) 2)
                        .build())
                .build();

        priceCalculator.calculateDiscount(check);

        assertEquals(0, check.getPrice().getFirst().getDiscount().compareTo(BigDecimal.TEN));
    }

    @Test
    public void calculateDiscount_ifDiscountCardAndNoWholesaleProduct() {
        Check check = Check.builder()
                .products(List.of(Product.builder()
                        .id(1)
                        .price(BigDecimal.valueOf(10))
                        .wholesaleProduct(false)
                        .description("test")
                        .quantity(10)
                        .build()))
                .discountCard(DiscountCard.builder()
                        .amount((short) 2)
                        .build())
                .build();

        priceCalculator.calculateDiscount(check);

        assertEquals(0, check.getPrice().getFirst().getDiscount().compareTo(BigDecimal.TWO));
    }

    @Test
    public void calculateDiscount_ifThereAreIdenticalWholesaleProducts() {
        Check check = Check.builder()
                .products(List.of(
                        Product.builder()
                                .id(1)
                                .price(BigDecimal.valueOf(10))
                                .wholesaleProduct(true)
                                .description("test")
                                .quantity(1)
                                .build(),

                        Product.builder()
                                .id(1)
                                .price(BigDecimal.valueOf(10))
                                .wholesaleProduct(true)
                                .description("test")
                                .quantity(4)
                                .build()

                ))
                .build();

        priceCalculator.calculateDiscount(check);

        assertEquals(0, check.getPrice().getFirst().getDiscount().compareTo(BigDecimal.ONE));
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

        assertEquals(0, check.getTotalPrice().compareTo(BigDecimal.valueOf(20)));
    }


    @Test
    public void calculateTotal_CheckDiscount() {
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

        assertEquals(0, check.getTotalDiscount().compareTo(BigDecimal.ONE));
    }

    @Test
    public void calculateTotal_CheckTotalWithDiscount() {
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

        assertEquals(0, check.getTotalWithDiscount().compareTo(BigDecimal.valueOf(19)));
    }


}