package ru.clevertec.check.service.impl;


import ru.clevertec.check.config.ApplicationConfig;
import ru.clevertec.check.ioc.annotation.Inject;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;
import ru.clevertec.check.model.Check;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.repository.DiscountCardRepository;
import ru.clevertec.check.repository.ProductRepository;
import ru.clevertec.check.repository.RepositoryException;
import ru.clevertec.check.service.CheckService;
import ru.clevertec.check.service.PriceCalculator;
import ru.clevertec.check.service.ServiceException;
import ru.clevertec.check.service.validator.Validator;
import ru.clevertec.check.util.ErrorCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoSpringComponent
public class CheckServiceImpl implements CheckService {
    @Inject
    private PriceCalculator priceCalculator;
    @Inject
    private ProductRepository productRepository;
    @Inject
    private DiscountCardRepository discountCardRepository;
    @Inject
    private Validator validator;
    private short discountDefault = ApplicationConfig.DISCOUNT_DEFAULT;


    public CheckServiceImpl() {
    }

    public CheckServiceImpl(PriceCalculator priceCalculator,
                            ProductRepository productRepository,
                            DiscountCardRepository discountCardRepository,
                            short discountDefault,
                            Validator validator) {
        this.priceCalculator = priceCalculator;
        this.productRepository = productRepository;
        this.discountCardRepository = discountCardRepository;
        this.discountDefault = discountDefault;
        this.validator = validator;
    }

    @Override
    public Check getCheck(Order order) {

        validate(order);

        List<Product> orderProducts = order.getProducts();

        Map<Long, Integer> groupProducts = groupByQuantityProducts(orderProducts);

        List<Product> stockProducts;
        try {
            stockProducts = productRepository.getProductsById(groupProducts.keySet());
        } catch (RepositoryException e) {
            throw new ServiceException("Can not create check for order" + order, e, ErrorCode.INTERNAL_SERVER_ERROR);
        }

        checkProductsInOrder(groupProducts, stockProducts);

        fillProductsInOrder(orderProducts, stockProducts);

        DiscountCard discountCard = getDiscountCard(Integer.valueOf(order.getDiscountCard()));

        Check check = Check.builder()
                .localDateTime(LocalDateTime.now())
                .products(orderProducts)
                .discountCard(discountCard)
                .build();

        priceCalculator.calculateDiscount(check);
        priceCalculator.calculateTotalPrice(check);

        BigDecimal balance = order.getBalanceDebitCard();
        BigDecimal totalPrice = check.getTotalWithDiscount();

        if (balance.compareTo(totalPrice) < 0) {
            throw new ServiceException(String.format("Total price is %s$ and balance is %s$", totalPrice, balance), ErrorCode.NOT_ENOUGH_MONEY);
        }

        return check;
    }

    private void validate(Order order) {
        List<String> errors = validator.validate(order);

        if (!errors.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            errors.forEach(error -> builder.append(error).append("; "));
            throw new ServiceException(builder.toString(), ErrorCode.BAD_REQUEST);
        }
    }

    private Map<Long, Integer> groupByQuantityProducts(List<Product> products) {
        return products.stream()
                .collect(Collectors.groupingBy(Product::getId, Collectors.summingInt(Product::getQuantity)));

    }

    private DiscountCard getDiscountCard(Integer numberDiscountCart) {
        DiscountCard discountCard = null;

        if (numberDiscountCart != null) {
            discountCard = discountCardRepository
                    .getDiscountCardByNumber(numberDiscountCart)
                    .orElseGet(() -> DiscountCard.builder()
                            .number(numberDiscountCart)
                            .amount(discountDefault).build()
                    );
        }
        return discountCard;
    }

    private void checkProductsInOrder(Map<Long, Integer> groupProducts, List<Product> stockProducts) throws ServiceException {
        StringBuilder errors = new StringBuilder();

        for (Product stockProduct : stockProducts) {
            Long productId = stockProduct.getId();

            int quantity = groupProducts.get(productId);
            int stockQuantity = stockProduct.getQuantity();

            groupProducts.remove(productId);

            if (stockQuantity < quantity) {
                errors.append(
                        String.format("Quantity (%d) of %s  is more than in stock (%d); ",
                                quantity,
                                stockProduct.getDescription(),
                                stockQuantity)
                );
            }
        }

        if (!groupProducts.isEmpty()) {
            throw new ServiceException("Unknown id product: " + groupProducts.values(), ErrorCode.BAD_REQUEST);
        }

        if (!errors.isEmpty()) {
            throw new ServiceException(errors.toString(), ErrorCode.BAD_REQUEST);
        }

    }

    private void fillProductsInOrder(List<Product> orderProducts, List<Product> stockProducts) {
        Map<Long, Product> productMap = stockProducts.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        orderProducts.forEach(product -> {
            Product stockProduct = productMap.get(product.getId());
            product.setWholesaleProduct(stockProduct.isWholesaleProduct());
            product.setDescription(stockProduct.getDescription());
            product.setPrice(stockProduct.getPrice());
        });
    }


}


