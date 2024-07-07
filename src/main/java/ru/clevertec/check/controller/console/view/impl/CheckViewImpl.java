package ru.clevertec.check.controller.console.view.impl;

import ru.clevertec.check.controller.console.view.CheckView;
import ru.clevertec.check.ioc.annotation.NoSpringComponent;
import ru.clevertec.check.model.Check;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Product;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

@NoSpringComponent
public class CheckViewImpl implements CheckView {
    @Override
    public String getCsv(Check check, String separator) {
        StringBuilder builder = new StringBuilder();

        LocalDateTime localDateTime = check.getLocalDateTime();

        builder.append("Date").append(separator).append("Time\n")
                .append(localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))).append(separator)
                .append(localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))).append("\n")
                .append("\n")
                .append("QTY").append(separator)
                .append("DESCRIPTION").append(separator)
                .append("PRICE").append(separator)
                .append("DISCOUNT").append(separator)
                .append("TOTAL\n");

        Iterator<Check.Price> iterator = check.getPrice().iterator();

        for (Product product : check.getProducts()) {
            builder.append(product.getQuantity()).append(separator)
                    .append(product.getDescription()).append(separator)
                    .append(product.getPrice().toString()).append("$").append(separator);

            Check.Price priceCheck = iterator.next();

            builder.append(priceCheck.getDiscount().toString()).append("$").append(separator)
                    .append(priceCheck.getTotal().toString()).append("$").append("\n");
        }

        DiscountCard discountCard = check.getDiscountCard();

        if (discountCard != null) {
            builder.append("\nDISCOUNT CARD").append(separator).append("DISCOUNT PERCENTAGE\n");

            String card = String.valueOf(discountCard.getNumber());
            int length = card.length();
            if (length < 4) {
                builder.append("0".repeat(4 - length));
            }
            builder.append(card).append(separator).append(discountCard.getAmount()).append("%\n");
        }

        builder.append("\nTOTAL PRICE").append(separator)
                .append("TOTAL DISCOUNT").append(separator)
                .append("TOTAL WITH DISCOUNT\n")
                .append(check.getTotalPrice().toString()).append("$").append(separator)
                .append(check.getTotalDiscount().toString()).append("$").append(separator)
                .append(check.getTotalWithDiscount().toString()).append("$");

        return builder.toString();
    }

    @Override
    public String getString(Check check) {
        return getCsv(check, " ");
    }
}
