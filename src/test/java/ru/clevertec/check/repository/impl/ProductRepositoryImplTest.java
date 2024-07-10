package ru.clevertec.check.repository.impl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.repository.DataSource;
import ru.clevertec.check.repository.ProductRepository;
import ru.clevertec.check.repository.connection.pool.ConnectionPool;
import ru.clevertec.check.repository.connection.pool.PoolData;
import ru.clevertec.check.repository.connection.pool.impl.ConnectionPoolImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductRepositoryImplTest {
    private static ProductRepository productRepository;

    private final static ConnectionPool connectionPool = new ConnectionPoolImpl();

    private Set<Long> setId;

    @BeforeAll
    public static void initConnectionPool() {

        PoolData poolData = PoolData.builder()
                .url(DataSource.URL)
                .user(DataSource.USER)
                .password(DataSource.PASSWORD)
                .poolSize(1)
                .build();
        connectionPool.initPoolData(poolData);

        productRepository = new ProductRepositoryImpl(connectionPool);

    }

    @BeforeEach
    public void initSet() {
        setId = new HashSet<>();
    }

    @Test
    public void getProductsById_findExists() {
        setId.add(1L);
        setId.add(2L);

        List<Product> products = productRepository.getProductsById(setId);


        assertArrayEquals(new Long[]{1L, 2L}, products.stream().map(Product::getId).toArray());
    }

    @Test
    public void getProductsById_findOneExistAndNoExist() {
        setId.add(1L);
        setId.add(-1L);

        List<Product> products = productRepository.getProductsById(setId);

        assertArrayEquals(new Long[]{1L}, products.stream().map(Product::getId).toArray());
    }


    @Test
    public void getProductsById_findNoExist() {
        setId.add(-1L);
        setId.add(-2L);

        List<Product> products = productRepository.getProductsById(setId);

        assertTrue(products.isEmpty());
    }


    @AfterAll
    public static void destroyConnectionPool() {

        connectionPool.destroy();

    }
}