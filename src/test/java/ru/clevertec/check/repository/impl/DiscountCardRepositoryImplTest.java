package ru.clevertec.check.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.repository.DiscountCardRepository;
import ru.clevertec.check.repository.RepositoryException;
import ru.clevertec.check.repository.connection.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DiscountCardRepositoryImplTest {

    private DiscountCardRepository discountCardRepository;
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
        discountCardRepository = new DiscountCardRepositoryImpl(connectionPool);
    }


    @Test
    void getDiscountCardByNumber_ifConnectionThrowSQLException() throws SQLException {
        Mockito.when(connectionPool.takeConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.any())).thenThrow(SQLException.class);

        assertThrows(RepositoryException.class, () -> discountCardRepository.getDiscountCardByNumber(1));
    }

    @Test
    void getDiscountCardByNumber_ifPreparedStatementThrowSQLException() throws SQLException {
        Mockito.when(connectionPool.takeConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.any())).thenReturn(preparedStatement);
        Mockito.doThrow(SQLException.class).when(preparedStatement).setInt(Mockito.anyInt(), Mockito.anyInt());

        assertThrows(RepositoryException.class, () -> discountCardRepository.getDiscountCardByNumber(1));
    }

    @Test
    void getDiscountCardByNumber_ifResulSetThrowSQLException() throws SQLException {
        Mockito.when(connectionPool.takeConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.any())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenThrow(SQLException.class);

        assertThrows(RepositoryException.class, () -> discountCardRepository.getDiscountCardByNumber(1));
    }

    @Test
    void getDiscountCardByNumber_ifResulSetGetLongThrowSQLException() throws SQLException {
        Mockito.when(connectionPool.takeConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.any())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getLong(Mockito.anyString())).thenThrow(SQLException.class);

        assertThrows(RepositoryException.class, () -> discountCardRepository.getDiscountCardByNumber(1));
    }

    @Test
    void getDiscountCardByNumber_ifResulSetGetIntThrowSQLException() throws SQLException {
        Mockito.when(connectionPool.takeConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.any())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getInt(Mockito.anyString())).thenThrow(SQLException.class);

        assertThrows(RepositoryException.class, () -> discountCardRepository.getDiscountCardByNumber(1));
    }

    @Test
    void getDiscountCardByNumber_ifResulSetGetShortThrowSQLException() throws SQLException {
        Mockito.when(connectionPool.takeConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.any())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getShort(Mockito.anyString())).thenThrow(SQLException.class);

        assertThrows(RepositoryException.class, () -> discountCardRepository.getDiscountCardByNumber(1));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void getDiscountCardByNumber_findExistValue(int number) throws SQLException {
        Mockito.when(connectionPool.takeConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.any())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getInt(Mockito.anyString())).thenReturn(number);

        DiscountCard discountCard = discountCardRepository.getDiscountCardByNumber(number).orElseThrow();

        assertEquals(number, discountCard.getNumber());
    }

    @Test
    void getDiscountCardByNumber_findNoExistValue() throws SQLException {
        Mockito.when(connectionPool.takeConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.any())).thenReturn(preparedStatement);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(false);

        Optional<DiscountCard> discountCardOptional = discountCardRepository.getDiscountCardByNumber(1);

        assertTrue(discountCardOptional.isEmpty());
    }


}