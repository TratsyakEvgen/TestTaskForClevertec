package ru.clevertec.check.model;


import ru.clevertec.check.service.validator.annotation.NotEmpty;
import ru.clevertec.check.service.validator.annotation.NotNull;
import ru.clevertec.check.service.validator.annotation.Pattern;
import ru.clevertec.check.service.validator.annotation.ValidCollection;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;


public class Order {

    @ValidCollection
    @NotEmpty(message = "List of products must not be empty")
    private List<Product> products;
    @Pattern(regexp = "\\d{4}", message = "Discount card number must consist of 4 digits")
    private String discountCard;
    @NotNull(message = "Debit card balance must not null")
    private BigDecimal balanceDebitCard;

    public Order() {
    }

    private Order(List<Product> products, String discountCard, BigDecimal balanceDebitCard) {
        this.products = products;
        this.discountCard = discountCard;
        this.balanceDebitCard = balanceDebitCard;
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public static class OrderBuilder {
        private List<Product> products;
        private String discountCard;
        private BigDecimal balanceDebitCard;

        OrderBuilder() {
        }

        public OrderBuilder products(List<Product> products) {
            this.products = products;
            return this;
        }

        public OrderBuilder discountCard(String discountCard) {
            this.discountCard = discountCard;
            return this;
        }

        public OrderBuilder balanceDebitCard(BigDecimal balanceDebitCard) {
            this.balanceDebitCard = balanceDebitCard;
            return this;
        }

        public Order build() {
            return new Order(this.products, this.discountCard, this.balanceDebitCard);
        }

    }

    public List<Product> getProducts() {
        return this.products;
    }

    public String getDiscountCard() {
        return this.discountCard;
    }

    public BigDecimal getBalanceDebitCard() {
        return this.balanceDebitCard;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setDiscountCard(String discountCard) {
        this.discountCard = discountCard;
    }

    public void setBalanceDebitCard(BigDecimal balanceDebitCard) {
        this.balanceDebitCard = balanceDebitCard;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Order order = (Order) object;
        return Objects.equals(products, order.products) &&
                Objects.equals(discountCard, order.discountCard) &&
                Objects.equals(balanceDebitCard, order.balanceDebitCard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(products, discountCard, balanceDebitCard);
    }

    @Override
    public String toString() {
        return "Order{" +
                "products=" + products +
                ", discountCard='" + discountCard + '\'' +
                ", balanceDebitCard=" + balanceDebitCard +
                '}';
    }
}
