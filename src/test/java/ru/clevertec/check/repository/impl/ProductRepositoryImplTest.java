package ru.clevertec.check.repository.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.repository.ProductRepository;
import ru.clevertec.check.repository.RepositoryException;
import ru.clevertec.check.repository.connection.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryImplTest {
    private ProductRepository productRepository;
    @Mock
    private ConnectionPool connectionPool;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setup() {
        productRepository = new ProductRepositoryImpl(connectionPool);
    }


    @Test
    void getProductsById_ifConnectionThrowSQLException() throws SQLException {
        Mockito.when(connectionPool.takeConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.any())).thenThrow(SQLException.class);

        assertThrows(RepositoryException.class, () -> productRepository.getProductsById(Set.of(1L)));
    }

    @Test
    void getProductsById_ifConnectionCreatArrayOfThrowSQLException() throws SQLException {
        Mockito.when(connectionPool.takeConnection()).thenReturn(connection);
        Mockito.when(connection.createArrayOf(Mockito.anyString(), Mockito.any())).thenThrow(SQLException.class);

        assertThrows(RepositoryException.class, () -> productRepository.getProductsById(Set.of(1L)));
    }


    @Test
    void getProductsById_ifPreparedStatementSetArrayThrowSQLException() throws SQLException {
        Mockito.when(connectionPool.takeConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.any())).thenReturn(preparedStatement);
        Mockito.doThrow(SQLException.class).when(preparedStatement).setArray(Mockito.anyInt(), Mockito.any());

        assertThrows(RepositoryException.class, () -> productRepository.getProductsById(Set.of(1L)));
    }

    @Test
    void getProductsById_ifResulSetThrowSQLException() throws SQLException {
        Mockito.when(connectionPool.takeConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.any())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenThrow(SQLException.class);

        assertThrows(RepositoryException.class, () -> productRepository.getProductsById(Set.of(1L)));
    }

    @Test
    void getProductsById_ifResulSetGetLongThrowSQLException() throws SQLException {
        Mockito.when(connectionPool.takeConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.any())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(Mockito.anyString())).thenThrow(SQLException.class);

        assertThrows(RepositoryException.class, () -> productRepository.getProductsById(Set.of(1L)));
    }

    @Test
    void getProductsById_ifResulSetGetStringThrowSQLException() throws SQLException {
        Mockito.when(connectionPool.takeConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.any())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getString(Mockito.anyString())).thenThrow(SQLException.class);

        assertThrows(RepositoryException.class, () -> productRepository.getProductsById(Set.of(1L)));
    }

    @Test
    void getProductsById_ifResulSetGetIntThrowSQLException() throws SQLException {
        Mockito.when(connectionPool.takeConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.any())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getInt(Mockito.anyString())).thenThrow(SQLException.class);

        assertThrows(RepositoryException.class, () -> productRepository.getProductsById(Set.of(1L)));
    }


    @Test
    void getProductsById_ifResulSetGetBigDecimalThrowSQLException() throws SQLException {
        Mockito.when(connectionPool.takeConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.any())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getBigDecimal(Mockito.anyString())).thenThrow(SQLException.class);

        assertThrows(RepositoryException.class, () -> productRepository.getProductsById(Set.of(1L)));
    }

    @Test
    void getProductsById_ifResulSetGetBooleanThrowSQLException() throws SQLException {
        Mockito.when(connectionPool.takeConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.any())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getBoolean(Mockito.anyString())).thenThrow(SQLException.class);

        assertThrows(RepositoryException.class, () -> productRepository.getProductsById(Set.of(1L)));
    }

    @ParameterizedTest
    @MethodSource("generateSetIdProducts")
    void getProductsById_findExistValue(Set<Long> setId) throws SQLException {
        Mockito.when(connectionPool.takeConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.any())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);


        Set<Long> copiedSetId = new HashSet<>(setId);
        Mockito.when(resultSet.next()).then(invocationOnMock -> !copiedSetId.isEmpty());
        Mockito.when(resultSet.getLong(Mockito.anyString()))
                .then(invocationOnMock -> {
                    Long id = copiedSetId.stream().findFirst().orElseThrow();
                    copiedSetId.remove(id);
                    return id;
                });

        Set<Long> productId = productRepository.getProductsById(setId)
                .stream()
                .map(Product::getId)
                .collect(Collectors.toSet());


        Assertions.assertEquals(setId, productId);
    }

    @Test
    void getProductsById_findNoExistValue() throws SQLException {
        Mockito.when(connectionPool.takeConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.any())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(false);

        List<Product> products = productRepository.getProductsById(Set.of(1L));

        assertTrue(products.isEmpty());
    }


    static Stream<Arguments> generateSetIdProducts() {
        return Stream.of(
                Arguments.of(new HashSet<>(Set.of(1L, 2L, 3L))),
                Arguments.of(new HashSet<>(Set.of(5L, 6L, 7L, 9L)))
        );
    }
}