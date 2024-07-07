package ru.clevertec.check.model;

import ru.clevertec.check.repository.csv.annotation.Csv;
import ru.clevertec.check.repository.csv.convertor.IntegerConvertor;
import ru.clevertec.check.repository.csv.convertor.LongConvertor;
import ru.clevertec.check.repository.csv.convertor.ShortConvertor;

import java.util.Objects;

public class DiscountCard {

    @Csv(converter = LongConvertor.class, column = "id")
    private long id;

    @Csv(converter = ShortConvertor.class, column = "discount amount, %")
    private short amount;

    @Csv(converter = IntegerConvertor.class, column = "number")
    private int number;

    public DiscountCard() {
    }

    private DiscountCard(long id, short amount, int number) {
        this.id = id;
        this.amount = amount;
        this.number = number;
    }


    public static DiscountCardBuilder builder() {
        return new DiscountCardBuilder();
    }

    public static class DiscountCardBuilder {
        private long id;
        private short amount;
        private int number;

        DiscountCardBuilder() {
        }

        public DiscountCardBuilder id(long id) {
            this.id = id;
            return this;
        }

        public DiscountCardBuilder amount(short amount) {
            this.amount = amount;
            return this;
        }

        public DiscountCardBuilder number(int number) {
            this.number = number;
            return this;
        }

        public DiscountCard build() {
            return new DiscountCard(this.id, this.amount, this.number);
        }

    }

    public long getId() {
        return this.id;
    }

    public short getAmount() {
        return this.amount;
    }

    public int getNumber() {
        return this.number;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAmount(short amount) {
        this.amount = amount;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        DiscountCard that = (DiscountCard) object;
        return id == that.id && amount == that.amount && number == that.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, number);
    }

    @Override
    public String toString() {
        return "DiscountCard{" +
                "id=" + id +
                ", amount=" + amount +
                ", number=" + number +
                '}';
    }
}
