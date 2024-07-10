package ru.clevertec.check.repository.impl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.repository.DataSource;
import ru.clevertec.check.repository.DiscountCardRepository;
import ru.clevertec.check.repository.connection.pool.ConnectionPool;
import ru.clevertec.check.repository.connection.pool.PoolData;
import ru.clevertec.check.repository.connection.pool.impl.ConnectionPoolImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DiscountCardRepositoryImplTest {
    private static DiscountCardRepository discountCardRepository;

    private final static ConnectionPool connectionPool = new ConnectionPoolImpl();

    @BeforeAll
    public static void initConnectionPool() {

        PoolData poolData = PoolData.builder()
                .url(DataSource.URL)
                .user(DataSource.USER)
                .password(DataSource.PASSWORD)
                .poolSize(1)
                .build();
        connectionPool.initPoolData(poolData);

        discountCardRepository = new DiscountCardRepositoryImpl(connectionPool);

    }

    @Test
    public void getDiscountCardByNumber_findExistValue() {
        Optional<DiscountCard> discountCardOptional = discountCardRepository.getDiscountCardByNumber(1111);

        assertFalse(discountCardOptional.isEmpty());

        DiscountCard discountCard = discountCardOptional.orElseThrow();
        assertEquals(1111, discountCard.getNumber());
    }

    @Test
    public void getDiscountCardByNumber_findNoExistValue() {
        Optional<DiscountCard> discountCardOptional = discountCardRepository.getDiscountCardByNumber(1);

        assertTrue(discountCardOptional.isEmpty());
    }


    @AfterAll
    public static void destroyConnectionPool() {

        connectionPool.destroy();

    }

}