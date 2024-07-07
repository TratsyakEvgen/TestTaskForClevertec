package ru.clevertec.check.repository.csv.convertor;

import ru.clevertec.check.repository.csv.Convertor;

public class LongConvertor implements Convertor {
    @Override
    public Object convert(String s) {
        return Long.valueOf(s);
    }
}
