package ru.clevertec.check.repository.csv.convertor;

import ru.clevertec.check.repository.csv.Convertor;

public class IntegerConvertor implements Convertor {
    @Override
    public Object convert(String s) {
        return Integer.parseInt(s);
    }
}
