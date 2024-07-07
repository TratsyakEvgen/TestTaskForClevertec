package ru.clevertec.check.repository.impl;

import ru.clevertec.check.config.ApplicationConfig;
import ru.clevertec.check.ioc.annotation.Inject;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.repository.ProductRepository;
import ru.clevertec.check.repository.RepositoryException;
import ru.clevertec.check.repository.csv.Csv;
import ru.clevertec.check.repository.csv.exeption.CsvParserException;
import ru.clevertec.check.repository.csv.impl.CsvFileParserImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoSpringComponent
public class ProductRepositoryImpl implements ProductRepository {
    @Inject
    private CsvFileParserImpl csvFileParserImpl;

    public ProductRepositoryImpl() {
    }

    public ProductRepositoryImpl(CsvFileParserImpl csvFileParserImpl) {
        this.csvFileParserImpl = csvFileParserImpl;
    }

    @Override
    public List<Product> getProductsById(Set<Long> setId) {

        try {

            Csv<Product> csv = csvFileParserImpl.parse(Product.class, ApplicationConfig.PRODUCTS);

            List<Product> productList = new ArrayList<>();

            for (Product product : csv) {
                if (setId.contains(product.getId())) {
                    productList.add(product);
                }
            }

            return productList;

        } catch (CsvParserException e) {
            throw new RepositoryException("Can not get list products by id " + setId, e);
        }
    }
}
