package ru.clevertec.check.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Check {

    private LocalDateTime localDateTime;
    private DiscountCard discountCard;
    private List<Product> products;
    private List<Price> price;
    private BigDecimal totalPrice;
    private BigDecimal totalDiscount;
    private BigDecimal totalWithDiscount;

    private Check(LocalDateTime localDateTime,
                  DiscountCard discountCard,
                  List<Product> products,
                  List<Price> price,
                  BigDecimal totalPrice,
                  BigDecimal totalDiscount,
                  BigDecimal totalWithDiscount) {
        this.localDateTime = localDateTime;
        this.discountCard = discountCard;
        this.products = products;
        this.price = price;
        this.totalPrice = totalPrice;
        this.totalDiscount = totalDiscount;
        this.totalWithDiscount = totalWithDiscount;
    }


    public static CheckBuilder builder() {
        return new CheckBuilder();
    }


    public static class CheckBuilder {
        private LocalDateTime localDateTime;
        private DiscountCard discountCard;
        private List<Product> products;
        private List<Price> price;
        private BigDecimal totalPrice;
        private BigDecimal totalDiscount;
        private BigDecimal totalWithDiscount;

        CheckBuilder() {
        }

        public CheckBuilder localDateTime(LocalDateTime localDateTime) {
            this.localDateTime = localDateTime;
            return this;
        }

        public CheckBuilder discountCard(DiscountCard discountCard) {
            this.discountCard = discountCard;
            return this;
        }

        public CheckBuilder products(List<Product> products) {
            this.products = products;
            return this;
        }

        public CheckBuilder price(List<Price> price) {
            this.price = price;
            return this;
        }

        public CheckBuilder totalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public CheckBuilder totalDiscount(BigDecimal totalDiscount) {
            this.totalDiscount = totalDiscount;
            return this;
        }

        public CheckBuilder totalWithDiscount(BigDecimal totalWithDiscount) {
            this.totalWithDiscount = totalWithDiscount;
            return this;
        }

        public Check build() {
            return new Check(
                    this.localDateTime,
                    this.discountCard,
                    this.products,
                    this.price,
                    this.totalPrice,
                    this.totalDiscount,
                    this.totalWithDiscount
            );
        }


    }

    public LocalDateTime getLocalDateTime() {
        return this.localDateTime;
    }

    public DiscountCard getDiscountCard() {
        return this.discountCard;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public List<Price> getPrice() {
        return this.price;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public BigDecimal getTotalDiscount() {
        return this.totalDiscount;
    }

    public BigDecimal getTotalWithDiscount() {
        return this.totalWithDiscount;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public void setDiscountCard(DiscountCard discountCard) {
        this.discountCard = discountCard;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setPrice(List<Price> price) {
        this.price = price;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public void setTotalWithDiscount(BigDecimal totalWithDiscount) {
        this.totalWithDiscount = totalWithDiscount;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Check check = (Check) object;
        return Objects.equals(localDateTime, check.localDateTime) && Objects.equals(discountCard, check.discountCard) && Objects.equals(products, check.products) && Objects.equals(price, check.price) && Objects.equals(totalPrice, check.totalPrice) && Objects.equals(totalDiscount, check.totalDiscount) && Objects.equals(totalWithDiscount, check.totalWithDiscount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(localDateTime, discountCard, products, price, totalPrice, totalDiscount, totalWithDiscount);
    }

    @Override
    public String toString() {
        return "Check{" +
                "localDateTime=" + localDateTime +
                ", discountCard=" + discountCard +
                ", products=" + products +
                ", price=" + price +
                ", totalPrice=" + totalPrice +
                ", totalDiscount=" + totalDiscount +
                ", totalWithDiscount=" + totalWithDiscount +
                '}';
    }

    public static class Price {
        private BigDecimal discount;
        private BigDecimal total;

        Price(BigDecimal discount, BigDecimal total) {
            this.discount = discount;
            this.total = total;
        }

        public static PriceBuilder builder() {
            return new PriceBuilder();
        }

        public static class PriceBuilder {
            private BigDecimal discount;
            private BigDecimal total;

            PriceBuilder() {
            }

            public PriceBuilder discount(BigDecimal discount) {
                this.discount = discount;
                return this;
            }

            public PriceBuilder total(BigDecimal total) {
                this.total = total;
                return this;
            }

            public Price build() {
                return new Price(this.discount, this.total);
            }

        }

        public BigDecimal getDiscount() {
            return this.discount;
        }

        public BigDecimal getTotal() {
            return this.total;
        }

        public void setDiscount(BigDecimal discount) {
            this.discount = discount;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }


        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            Price price = (Price) object;
            return Objects.equals(discount, price.discount) && Objects.equals(total, price.total);
        }

        @Override
        public int hashCode() {
            return Objects.hash(discount, total);
        }

        @Override
        public String toString() {
            return "Price{" +
                    "discount=" + discount +
                    ", total=" + total +
                    '}';
        }


    }

}
