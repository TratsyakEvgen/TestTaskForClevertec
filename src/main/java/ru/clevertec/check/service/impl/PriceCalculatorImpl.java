package ru.clevertec.check.service.impl;

import ru.clevertec.check.config.ApplicationConfig;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;
import ru.clevertec.check.model.Check;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.service.PriceCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoSpringComponent
public class PriceCalculatorImpl implements PriceCalculator {

    private final BigDecimal wholesaleDiscount;
    private final int wholesaleQuantity;


    public PriceCalculatorImpl() {
        this.wholesaleDiscount = new BigDecimal(ApplicationConfig.WHOLESALE_DISCOUNT / 100);
        this.wholesaleQuantity = ApplicationConfig.WHOLESALE_QUANTITY;
    }

    public PriceCalculatorImpl(BigDecimal wholesaleDiscount, int wholesaleQuantity) {
        this.wholesaleDiscount = wholesaleDiscount;
        this.wholesaleQuantity = wholesaleQuantity;
    }

    @Override
    public void calculateDiscount(Check check) {
        List<Product> products = check.getProducts();

        Map<Long, Integer> groupProducts = products.stream()
                .collect(Collectors.groupingBy(Product::getId, Collectors.summingInt(Product::getQuantity)));

        List<Check.Price> mapPrice = products.stream()
                .map(product -> {
                            int totalQuantity = groupProducts.get(product.getId());

                            int quantity = product.getQuantity();

                            BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(quantity));
                            Check.Price.PriceBuilder priceBuilder = Check.Price.builder().total(total);

                            BigDecimal discount = BigDecimal.ZERO;
                            if (totalQuantity >= wholesaleQuantity && product.isWholesaleProduct()) {
                                discount = total.multiply(this.wholesaleDiscount);
                                return priceBuilder.discount(discount.setScale(2, RoundingMode.HALF_UP)).build();
                            }

                            DiscountCard discountCard = check.getDiscountCard();
                            if (discountCard != null) {
                                double amount = discountCard.getAmount();
                                discount = total.multiply(BigDecimal.valueOf(amount / 100));
                            }

                            return priceBuilder.discount(discount.setScale(2, RoundingMode.HALF_UP)).build();
                        }
                )
                .collect(Collectors.toList());


        check.setPrice(mapPrice);
    }

    @Override
    public void calculateTotalPrice(Check check) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;


        for (Check.Price price : check.getPrice()) {
            totalPrice = totalPrice.add(price.getTotal());
            totalDiscount = totalDiscount.add(price.getDiscount());
        }

        BigDecimal totalWithDiscount = totalPrice.subtract(totalDiscount);

        check.setTotalPrice(totalPrice);
        check.setTotalDiscount(totalDiscount);
        check.setTotalWithDiscount(totalWithDiscount);


    }
}
