package ru.clevertec.check.repository.csv.convertor;

import ru.clevertec.check.repository.csv.Convertor;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalConvertor implements Convertor {
    @Override
    public Object convert(String s) {
        return new BigDecimal(s.replace(",", ".")).setScale(2, RoundingMode.HALF_UP);
    }
}
