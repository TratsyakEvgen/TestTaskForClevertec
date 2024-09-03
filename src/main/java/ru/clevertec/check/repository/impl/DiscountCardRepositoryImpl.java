package ru.clevertec.check.repository.impl;


import ru.clevertec.check.ioc.annotation.Inject;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.repository.DiscountCardRepository;
import ru.clevertec.check.repository.RepositoryException;
import ru.clevertec.check.repository.connection.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@NoSpringComponent
public class DiscountCardRepositoryImpl implements DiscountCardRepository {
    @Inject
    private ConnectionPool connectionPool;


    public DiscountCardRepositoryImpl() {
    }

    public DiscountCardRepositoryImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Optional<DiscountCard> getDiscountCardByNumber(int number) {

        try (Connection connection = connectionPool.takeConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM discount_card WHERE discount_card.number = ?")) {

            statement.setInt(1, number);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return Optional.ofNullable(DiscountCard.builder()
                            .id(result.getLong("id"))
                            .number(result.getInt("number"))
                            .amount(result.getShort("amount"))
                            .build());
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error find discount card by number", e);
        }
    }
}
