CREATE DATABASE test
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;


CREATE TABLE IF NOT EXISTS public.product
(
    id                BIGSERIAL PRIMARY KEY,
    description       VARCHAR(50)    NOT NULL
        CONSTRAINT description_not_empty CHECK (regexp_like(description, '[^\s\t\n\r\f]+.*')),
    price             DECIMAL(16, 2) NOT NULL
        CONSTRAINT price_min CHECK (price >= 0.00),
    quantity_in_stock INTEGER        NOT NULL
        CONSTRAINT quantity_in_stock_min CHECK (quantity_in_stock >= 0),
    wholesale_product BOOLEAN        NOT NULL
);

CREATE TABLE IF NOT EXISTS public.discount_card
(
    id     BIGSERIAL PRIMARY KEY,
    number INTEGER  NOT NULL UNIQUE
        CONSTRAINT number_size CHECK (number >= 0 AND number <= 9999),
    amount SMALLINT NOT NULL
        CONSTRAINT amount_size CHECK ( amount >= 0 AND amount <= 100 )
);

COPY product (id, description, price, quantity_in_stock, wholesale_product)
    FROM 'C:\check\products.csv'
    DELIMITER ';'
    CSV HEADER;

COPY discount_card (id, number, amount)
    FROM 'C:\check\discountCards.csv'
    DELIMITER ';'
    CSV HEADER;


SELECT *
FROM product
WHERE product.id = ANY (?);

SELECT *
FROM discount_card
WHERE discount_card.number = ?;