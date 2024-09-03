package ru.clevertec.check.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
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
import static org.mockito.Mockito.*;

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
        when(connectionPool.takeConnection()).thenReturn(connection);
        when(connection.prepareStatement(any())).thenThrow(SQLException.class);

        assertThrows(RepositoryException.class, () -> discountCardRepository.getDiscountCardByNumber(1));
    }

    @Test
    void getDiscountCardByNumber_ifPreparedStatementThrowSQLException() throws SQLException {
        when(connectionPool.takeConnection()).thenReturn(connection);
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);
        doThrow(SQLException.class).when(preparedStatement).setInt(anyInt(), anyInt());

        assertThrows(RepositoryException.class, () -> discountCardRepository.getDiscountCardByNumber(1));
    }

    @Test
    void getDiscountCardByNumber_ifResulSetThrowSQLException() throws SQLException {
        when(connectionPool.takeConnection()).thenReturn(connection);
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenThrow(SQLException.class);

        assertThrows(RepositoryException.class, () -> discountCardRepository.getDiscountCardByNumber(1));
    }

    @Test
    void getDiscountCardByNumber_ifResulSetGetLongThrowSQLException() throws SQLException {
        when(connectionPool.takeConnection()).thenReturn(connection);
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(anyString())).thenThrow(SQLException.class);

        assertThrows(RepositoryException.class, () -> discountCardRepository.getDiscountCardByNumber(1));
    }

    @Test
    void getDiscountCardByNumber_ifResulSetGetIntThrowSQLException() throws SQLException {
        when(connectionPool.takeConnection()).thenReturn(connection);
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(anyString())).thenThrow(SQLException.class);

        assertThrows(RepositoryException.class, () -> discountCardRepository.getDiscountCardByNumber(1));
    }

    @Test
    void getDiscountCardByNumber_ifResulSetGetShortThrowSQLException() throws SQLException {
        when(connectionPool.takeConnection()).thenReturn(connection);
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getShort(anyString())).thenThrow(SQLException.class);

        assertThrows(RepositoryException.class, () -> discountCardRepository.getDiscountCardByNumber(1));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void getDiscountCardByNumber_findExistValue(int number) throws SQLException {
        when(connectionPool.takeConnection()).thenReturn(connection);
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(anyString())).thenReturn(number);

        DiscountCard discountCard = discountCardRepository.getDiscountCardByNumber(number).orElseThrow();

        assertEquals(number, discountCard.getNumber());
    }

    @Test
    void getDiscountCardByNumber_findNoExistValue() throws SQLException {
        when(connectionPool.takeConnection()).thenReturn(connection);
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Optional<DiscountCard> discountCardOptional = discountCardRepository.getDiscountCardByNumber(1);

        assertTrue(discountCardOptional.isEmpty());
    }


}