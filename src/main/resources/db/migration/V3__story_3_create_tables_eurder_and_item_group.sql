CREATE TABLE eurder
(
    id          UUID NOT NULL,
    customer_id UUID NOT NULL,
    CONSTRAINT pk_eurder PRIMARY KEY (id),
    CONSTRAINT fk_eurder_customer FOREIGN KEY (customer_id) REFERENCES customer (id)
);

CREATE TABLE item_group
(
    eurder_id     UUID           NOT NULL,
    line_number   INTEGER        NOT NULL,
    item_id       UUID           NOT NULL,
    quantity      INTEGER        NOT NULL,
    price         NUMERIC(10, 2) NOT NULL,
    shipping_date DATE           NOT NULL,
    CONSTRAINT pk_item_group PRIMARY KEY (eurder_id, line_number),
    CONSTRAINT fk_item_group_eurder FOREIGN KEY (eurder_id) REFERENCES eurder (id),
    CONSTRAINT fk_item_group_item FOREIGN KEY (item_id) REFERENCES item (id),
    CONSTRAINT ck_item_group_quantity_positive CHECK (quantity > 0)
);
