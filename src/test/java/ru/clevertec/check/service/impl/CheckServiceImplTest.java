package ru.clevertec.check.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.config.ApplicationConfig;
import ru.clevertec.check.model.Check;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckServiceImplTest {
    @Mock
    private Validator validator;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private DiscountCardRepository discountCardRepository;
    @Mock
    private PriceCalculator priceCalculator;
    @Mock
    private Order order;

    private CheckService checkService;

    @BeforeEach
    public void init() {
        checkService = new CheckServiceImpl(priceCalculator,
                productRepository,
                discountCardRepository,
                ApplicationConfig.DISCOUNT_DEFAULT,
                validator);
    }

    @Test
    void getCheck_NoValidData() {
        when(validator.validate(Mockito.any())).thenReturn(List.of("validation error message"));

        ServiceException exception = assertThrows(ServiceException.class, () -> checkService.getCheck(Mockito.any()));

        assertEquals(ErrorCode.BAD_REQUEST, exception.getErrorCode());
    }

    @Test
    void getCheck_ProductRepositoryException() {
        when(validator.validate(Mockito.any())).thenReturn(Collections.emptyList());
        when(productRepository.getProductsById(Collections.emptySet())).thenThrow(RepositoryException.class);

        ServiceException exception = assertThrows(ServiceException.class, () -> checkService.getCheck(order));

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
    }

    @Test
    void getCheck_DiscountCardRepositoryException() {
        when(validator.validate(Mockito.any())).thenReturn(Collections.emptyList());
        when(productRepository.getProductsById(Collections.emptySet())).thenReturn(Collections.emptyList());
        when(discountCardRepository.getDiscountCardByNumber(Mockito.anyInt())).thenThrow(RepositoryException.class);
        when(order.getDiscountCard()).thenReturn("1000");

        ServiceException exception = assertThrows(ServiceException.class, () -> checkService.getCheck(order));

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
    }


    @Test
    void getCheck_IncorrectIDProductsInOrder() {
        when(validator.validate(Mockito.any())).thenReturn(Collections.emptyList());
        when(order.getProducts()).thenReturn(List.of(Product.builder().id(100).build()));
        when(productRepository.getProductsById(Mockito.any())).thenReturn(Collections.emptyList());

        ServiceException exception = assertThrows(ServiceException.class, () -> checkService.getCheck(order));

        assertEquals(ErrorCode.BAD_REQUEST, exception.getErrorCode());
    }

    @Test
    void getCheck_IncorrectQuantityProductsInOrder() {
        when(validator.validate(Mockito.any())).thenReturn(Collections.emptyList());
        when(order.getProducts()).thenReturn(List.of(Product.builder().id(1L).quantity(5).build()));
        when(productRepository.getProductsById(Mockito.any())).thenReturn(List.of(Product.builder().id(1L).quantity(4).build()));


        ServiceException exception = assertThrows(ServiceException.class, () -> checkService.getCheck(order));

        assertEquals(ErrorCode.BAD_REQUEST, exception.getErrorCode());
    }


    @Test
    void getCheck_CheckDefaultDiscountCard() {
        when(validator.validate(Mockito.any())).thenReturn(Collections.emptyList());
        when(order.getDiscountCard()).thenReturn("1000");
        when(order.getProducts()).thenReturn(List.of(Product.builder().id(1L).quantity(5).build()));
        when(productRepository.getProductsById(Mockito.any())).thenReturn(List.of(Product.builder().id(1L).quantity(5).build()));
        when(order.getBalanceDebitCard()).thenReturn(BigDecimal.ONE);
        doAnswer(invocationOnMock -> {
                    Check check = invocationOnMock.getArgument(0, Check.class);
                    check.setTotalWithDiscount(BigDecimal.ONE);
                    return check;
                }
        ).when(priceCalculator).calculateTotalPrice(Mockito.any());

        when(discountCardRepository.getDiscountCardByNumber(Mockito.anyInt())).thenReturn(Optional.empty());


        Check check = checkService.getCheck(order);

        assertEquals(ApplicationConfig.DISCOUNT_DEFAULT, check.getDiscountCard().getAmount());
    }


    @Test
    void getCheck_NotEnoughMoney() {
        when(validator.validate(Mockito.any())).thenReturn(Collections.emptyList());
        when(order.getDiscountCard()).thenReturn("1000");
        when(order.getProducts()).thenReturn(List.of(Product.builder().id(1L).quantity(5).build()));
        when(productRepository.getProductsById(Mockito.any())).thenReturn(List.of(Product.builder().id(1L).quantity(5).build()));
        when(discountCardRepository.getDiscountCardByNumber(Mockito.anyInt())).thenReturn(Optional.empty());

        when(order.getBalanceDebitCard()).thenReturn(BigDecimal.ONE);
        doAnswer(invocationOnMock -> {
                    Check check = invocationOnMock.getArgument(0, Check.class);
                    check.setTotalWithDiscount(BigDecimal.TWO);
                    return check;
                }
        ).when(priceCalculator).calculateTotalPrice(Mockito.any());


        ServiceException exception = assertThrows(ServiceException.class, () -> checkService.getCheck(order));

        assertEquals(ErrorCode.NOT_ENOUGH_MONEY, exception.getErrorCode());
    }
}