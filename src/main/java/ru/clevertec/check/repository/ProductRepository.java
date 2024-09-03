package ru.clevertec.check.repository;

import ru.clevertec.check.model.Product;

import java.util.List;
import java.util.Set;

public interface ProductRepository {
    List<Product> getProductsById(Set<Long> setId);
}
