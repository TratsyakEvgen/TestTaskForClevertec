package ru.clevertec.check.repository.csv.convertor;

import ru.clevertec.check.repository.csv.Convertor;

public class WholesaleProductConvertor implements Convertor {

    @Override
    public Object convert(String s) {
        return (s.equals("+")) ? Boolean.TRUE : Boolean.FALSE;
    }
}

