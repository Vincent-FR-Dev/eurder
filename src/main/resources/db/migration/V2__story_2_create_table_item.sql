CREATE TABLE item
(
    id          UUID          NOT NULL,
    name        VARCHAR(255)  NOT NULL,
    description VARCHAR(1000) NOT NULL,
    price       NUMERIC(10, 2) NOT NULL,
    stock       INTEGER       NOT NULL,
    CONSTRAINT pk_item PRIMARY KEY (id),
    CONSTRAINT ck_item_price_positive CHECK (price > 0),
    CONSTRAINT ck_item_stock_not_negative CHECK (stock >= 0)
);
