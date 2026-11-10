CREATE TABLE customer
(
    id            UUID         NOT NULL,
    firstname     VARCHAR(100) NOT NULL,
    lastname      VARCHAR(100) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    street_name   VARCHAR(255) NOT NULL,
    street_number VARCHAR(20)  NOT NULL,
    postal_code   VARCHAR(20)  NOT NULL,
    city          VARCHAR(100) NOT NULL,
    phone_number  VARCHAR(30)  NOT NULL,
    password      VARCHAR(100) NOT NULL,
    CONSTRAINT pk_customer PRIMARY KEY (id),
    CONSTRAINT uk_customer_email UNIQUE (email)
);
