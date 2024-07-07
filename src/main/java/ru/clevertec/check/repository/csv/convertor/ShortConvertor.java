package ru.clevertec.check.repository.csv.convertor;

import ru.clevertec.check.repository.csv.Convertor;

public class ShortConvertor implements Convertor {
    @Override
    public Object convert(String s) {
        return Short.parseShort(s);
    }
}
