package ru.clevertec.check.repository.impl;


import ru.clevertec.check.ioc.annotation.Inject;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.repository.ProductRepository;
import ru.clevertec.check.repository.RepositoryException;
import ru.clevertec.check.repository.connection.pool.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoSpringComponent
public class ProductRepositoryImpl implements ProductRepository {
    @Inject
    private ConnectionPool connectionPool;

    public ProductRepositoryImpl() {
    }

    public ProductRepositoryImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public List<Product> getProductsById(Set<Long> setId) {

        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM product WHERE product.id = ANY(?)")) {

            Array array = connection.createArrayOf("BIGINT", setId.toArray());

            statement.setArray(1, array);


            try (ResultSet result = statement.executeQuery()) {

                List<Product> products = new ArrayList<>();

                while (result.next()) {
                    Product product = Product.builder()
                            .id(result.getLong("id"))
                            .description(result.getString("description"))
                            .quantity(result.getInt("quantity_in_stock"))
                            .price(result.getBigDecimal("price"))
                            .wholesaleProduct(result.getBoolean("wholesale_product"))
                            .build();
                    products.add(product);
                }

                return products;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error find products by id", e);
        }
    }
}
