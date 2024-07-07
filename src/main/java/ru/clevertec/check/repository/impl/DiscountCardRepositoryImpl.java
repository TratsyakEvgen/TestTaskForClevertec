package ru.clevertec.check.repository.impl;


import ru.clevertec.check.config.ApplicationConfig;
import ru.clevertec.check.ioc.annotation.Inject;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.repository.DiscountCardRepository;
import ru.clevertec.check.repository.RepositoryException;
import ru.clevertec.check.repository.csv.Csv;
import ru.clevertec.check.repository.csv.exeption.CsvParserException;
import ru.clevertec.check.repository.csv.impl.CsvFileParserImpl;

import java.util.Optional;

@NoSpringComponent
public class DiscountCardRepositoryImpl implements DiscountCardRepository {

    @Inject
    private CsvFileParserImpl csvFileParserImpl;

    public DiscountCardRepositoryImpl() {
    }

    public DiscountCardRepositoryImpl(CsvFileParserImpl csvFileParserImpl) {
        this.csvFileParserImpl = csvFileParserImpl;
    }

    @Override
    public Optional<DiscountCard> getDiscountCardByNumber(int number) {

        try {

            Csv<DiscountCard> csv = csvFileParserImpl.parse(DiscountCard.class, ApplicationConfig.DISCOUNT_CARDS);

            for (DiscountCard discountCard : csv) {
                if (discountCard.getNumber() == number) {
                    return Optional.of(discountCard);
                }
            }

        } catch (CsvParserException e) {
            throw new RepositoryException("Can not get discount card by number " + number, e);
        }

        return Optional.empty();
    }
}
