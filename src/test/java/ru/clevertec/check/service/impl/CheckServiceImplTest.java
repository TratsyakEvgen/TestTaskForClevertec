package ru.clevertec.check.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.config.ApplicationConfig;
import ru.clevertec.check.model.Check;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.repository.DiscountCardRepository;
import ru.clevertec.check.repository.ProductRepository;
import ru.clevertec.check.repository.RepositoryException;
import ru.clevertec.check.service.CheckService;
import ru.clevertec.check.service.ServiceException;
import ru.clevertec.check.service.validator.Validator;
import ru.clevertec.check.util.ErrorCode;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckServiceImplTest {
    @Mock
    private Validator validator;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private DiscountCardRepository discountCardRepository;

    private CheckService checkService;

    @BeforeEach
    public void init() {
        checkService = new CheckServiceImpl(new PriceCalculatorImpl(),
                productRepository,
                discountCardRepository,
                ApplicationConfig.DISCOUNT_DEFAULT,
                validator);
    }

    @Test
    void getCheck_NoValidData() {
        Order mockOrder = mock(Order.class);
        when(validator.validate(mockOrder)).thenReturn(List.of("validation error message"));

        ServiceException exception = assertThrows(ServiceException.class, () -> checkService.getCheck(mockOrder));

        assertEquals(ErrorCode.BAD_REQUEST, exception.getErrorCode());
    }

    @Test
    void getCheck_ProductRepositoryException() {
        Order mockOrder = mock(Order.class);
        when(validator.validate(mockOrder)).thenReturn(Collections.emptyList());
        when(productRepository.getProductsById(Collections.emptySet())).thenThrow(RepositoryException.class);

        ServiceException exception = assertThrows(ServiceException.class, () -> checkService.getCheck(mockOrder));

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
    }

    @Test
    void getCheck_DiscountCardRepositoryException() {
        Order order = Order.builder().discountCard("1000").products(Collections.emptyList()).build();
        when(validator.validate(order)).thenReturn(Collections.emptyList());
        when(productRepository.getProductsById(Collections.emptySet())).thenReturn(Collections.emptyList());
        when(discountCardRepository.getDiscountCardByNumber(1000)).thenThrow(RepositoryException.class);

        ServiceException exception = assertThrows(ServiceException.class, () -> checkService.getCheck(order));

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
    }


    @Test
    void getCheck_IncorrectIDProductsInOrder() {
        Order order = Order.builder().products(List.of(Product.builder().id(100).build())).build();
        when(validator.validate(order)).thenReturn(Collections.emptyList());
        when(productRepository.getProductsById(Set.of(100L))).thenReturn(Collections.emptyList());

        ServiceException exception = assertThrows(ServiceException.class, () -> checkService.getCheck(order));

        assertEquals(ErrorCode.BAD_REQUEST, exception.getErrorCode());
    }

    @Test
    void getCheck_IncorrectQuantityProductsInOrder() {
        Order order = Order.builder().products(List.of(Product.builder().id(1L).quantity(5).build())).build();
        when(validator.validate(order)).thenReturn(Collections.emptyList());
        when(productRepository.getProductsById(Set.of(1L))).thenReturn(List.of(Product.builder().id(1L).quantity(4).build()));


        ServiceException exception = assertThrows(ServiceException.class, () -> checkService.getCheck(order));

        assertEquals(ErrorCode.BAD_REQUEST, exception.getErrorCode());
    }


    @Test
    void getCheck_CheckDefaultDiscountCard() {
        Product product = Product.builder()
                .id(1)
                .price(BigDecimal.valueOf(10))
                .wholesaleProduct(false)
                .description("test")
                .quantity(10)
                .build();
        Order order = Order.builder().discountCard("1000").balanceDebitCard(BigDecimal.valueOf(1000)).products(List.of(product)).build();
        when(validator.validate(order)).thenReturn(Collections.emptyList());
        when(productRepository.getProductsById(Set.of(product.getId()))).thenReturn(List.of(product));
        when(discountCardRepository.getDiscountCardByNumber(1000)).thenReturn(Optional.empty());

        Check check = checkService.getCheck(order);

        assertEquals(ApplicationConfig.DISCOUNT_DEFAULT, check.getDiscountCard().getAmount());
    }


    @Test
    void getCheck_NotEnoughMoney() {
        Product product = Product.builder()
                .id(1)
                .price(BigDecimal.valueOf(10))
                .wholesaleProduct(false)
                .description("test")
                .quantity(10)
                .build();
        Order order = Order.builder().discountCard("1000").balanceDebitCard(BigDecimal.ZERO).products(List.of(product)).build();
        when(validator.validate(order)).thenReturn(Collections.emptyList());
        when(productRepository.getProductsById(Set.of(product.getId()))).thenReturn(List.of(product));
        when(discountCardRepository.getDiscountCardByNumber(1000)).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> checkService.getCheck(order));

        assertEquals(ErrorCode.NOT_ENOUGH_MONEY, exception.getErrorCode());
    }
}