package ru.clevertec.check.controller.console.args.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.controller.console.args.ObjectDeserializer;
import ru.clevertec.check.controller.console.args.ObjectMapper;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.model.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class OrderObjectMapperTest {

    private ObjectMapper<Order> objectMapper;

    @Mock
    private ObjectDeserializer<List<Product>> listObjectDeserializer;

    @Mock
    private ObjectDeserializer<String> stringObjectDeserializer;

    @Mock
    private ObjectDeserializer<BigDecimal> bigDecimalObjectDeserializer;

    @BeforeEach
    public void setup() {
        objectMapper = new OrderObjectMapper(
                listObjectDeserializer,
                stringObjectDeserializer,
                bigDecimalObjectDeserializer);
    }

    @Test
    void deserializer() {
        List<Product> products = List.of(Product.builder().id(3L).quantity(1).build());
        BigDecimal balanceCard = BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_UP);
        String discountCard = "1111";

        Mockito.when(listObjectDeserializer.deserializer(Mockito.any(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(products);
        Mockito.when(bigDecimalObjectDeserializer.deserializer(Mockito.any(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(balanceCard);
        Mockito.when(stringObjectDeserializer.deserializer(Mockito.any(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(discountCard);

        Order actual = objectMapper.getObject(new String[]{});


        assertEquals(
                Order.builder()
                        .products(products)
                        .balanceDebitCard(balanceCard)
                        .discountCard(discountCard)
                        .build(),
                actual
        );
    }

}