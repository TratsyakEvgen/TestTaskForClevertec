package ru.clevertec.check.model;

import ru.clevertec.check.repository.csv.annotation.Csv;
import ru.clevertec.check.repository.csv.convertor.*;
import ru.clevertec.check.service.validator.annotation.Min;

import java.math.BigDecimal;
import java.util.Objects;


public class Product {

    @Csv(converter = LongConvertor.class, column = "id")
    @Min(value = 1, message = "Id of products must not be null")
    private long id;

    @Csv(converter = StringConvertor.class, column = "description")
    private String description;

    @Csv(converter = BigDecimalConvertor.class, column = "price, $")
    private BigDecimal price;

    @Csv(converter = IntegerConvertor.class, column = "quantity in stock")
    @Min(value = 1, message = "Quantity of products must not be null")
    private int quantity;

    @Csv(converter = WholesaleProductConvertor.class, column = "wholesale product")
    private boolean wholesaleProduct;

    public Product() {
    }

    private Product(long id, String description, BigDecimal price, int quantity, boolean wholesaleProduct) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.wholesaleProduct = wholesaleProduct;
    }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public static class ProductBuilder {
        private long id;
        private String description;
        private BigDecimal price;
        private int quantity;
        private boolean wholesaleProduct;

        ProductBuilder() {
        }

        public ProductBuilder id(long id) {
            this.id = id;
            return this;
        }

        public ProductBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ProductBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ProductBuilder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public ProductBuilder wholesaleProduct(boolean wholesaleProduct) {
            this.wholesaleProduct = wholesaleProduct;
            return this;
        }

        public Product build() {
            return new Product(this.id, this.description, this.price, this.quantity, this.wholesaleProduct);
        }

    }

    public long getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public boolean isWholesaleProduct() {
        return this.wholesaleProduct;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setWholesaleProduct(boolean wholesaleProduct) {
        this.wholesaleProduct = wholesaleProduct;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Product product = (Product) object;
        return id == product.id &&
                quantity == product.quantity &&
                wholesaleProduct == product.wholesaleProduct &&
                Objects.equals(description, product.description) &&
                Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, price, quantity, wholesaleProduct);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", wholesaleProduct=" + wholesaleProduct +
                '}';
    }
}
