package ru.clevertec.check.repository.csv;


import java.lang.reflect.Field;
import java.util.Objects;


public class Header {
    private int position;
    private Field field;
    private Convertor convertor;

    private Header(int position, Field field, Convertor convertor) {
        this.position = position;
        this.field = field;
        this.convertor = convertor;
    }

    public static HeaderBuilder builder() {
        return new HeaderBuilder();
    }

    public static class HeaderBuilder {
        private int position;
        private Field field;
        private Convertor convertor;

        HeaderBuilder() {
        }

        public HeaderBuilder position(int position) {
            this.position = position;
            return this;
        }

        public HeaderBuilder field(Field field) {
            this.field = field;
            return this;
        }

        public HeaderBuilder converter(Convertor convertor) {
            this.convertor = convertor;
            return this;
        }

        public Header build() {
            return new Header(this.position, this.field, this.convertor);
        }

    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Convertor getConverter() {
        return convertor;
    }

    public void setConverter(Convertor convertor) {
        this.convertor = convertor;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Header header = (Header) object;
        return position == header.position && Objects.equals(field, header.field) && Objects.equals(convertor, header.convertor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, field, convertor);
    }

    @Override
    public String toString() {
        return "Header{" +
                "position=" + position +
                ", field=" + field +
                ", converter=" + convertor +
                '}';
    }
}
