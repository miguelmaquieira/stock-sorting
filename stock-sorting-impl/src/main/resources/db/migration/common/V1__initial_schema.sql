CREATE TABLE products
(
    id    BIGINT PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    sales INT          NOT NULL
);

CREATE TABLE product_stock
(
    product_id BIGINT      NOT NULL,
    size       VARCHAR(10) NOT NULL,
    quantity   INT         NOT NULL,
    PRIMARY KEY (product_id, size),
    CONSTRAINT fk_product_stock_product FOREIGN KEY (product_id)
        REFERENCES products (id)
        ON DELETE CASCADE
);
