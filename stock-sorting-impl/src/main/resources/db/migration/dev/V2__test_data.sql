-- Clear previous data
DELETE FROM products;
DELETE FROM product_stock;

-- Insert products
INSERT INTO products (id, name, sales) VALUES (1, 'V-NECH BASIC SHIRT', 100);
INSERT INTO products (id, name, sales) VALUES (2, 'CONTRASTING FABRIC T-SHIRT', 50);
INSERT INTO products (id, name, sales) VALUES (3, 'RAISED PRINT T-SHIRT', 80);
INSERT INTO products (id, name, sales) VALUES (4, 'PLEATED T-SHIRT', 3);
INSERT INTO products (id, name, sales) VALUES (5, 'CONTRASTING LACE T-SHIRT', 650);
INSERT INTO products (id, name, sales) VALUES (6, 'SLOGAN T-SHIRT', 20);

-- Insert stock values for each product
-- Format: product_id, size, quantity

-- Product 1 stock: S:4 / M:9 / L:0
INSERT INTO product_stock (product_id, size, quantity) VALUES (1, 'S', 4);
INSERT INTO product_stock (product_id, size, quantity) VALUES (1, 'M', 9);
INSERT INTO product_stock (product_id, size, quantity) VALUES (1, 'L', 0);

-- Product 2 stock: S:35 / M:9 / L:9
INSERT INTO product_stock (product_id, size, quantity) VALUES (2, 'S', 35);
INSERT INTO product_stock (product_id, size, quantity) VALUES (2, 'M', 9);
INSERT INTO product_stock (product_id, size, quantity) VALUES (2, 'L', 9);

-- Product 3 stock: S:20 / M:2 / L:20
INSERT INTO product_stock (product_id, size, quantity) VALUES (3, 'S', 20);
INSERT INTO product_stock (product_id, size, quantity) VALUES (3, 'M', 2);
INSERT INTO product_stock (product_id, size, quantity) VALUES (3, 'L', 20);

-- Product 4 stock: S:25 / M:30 / L:10
INSERT INTO product_stock (product_id, size, quantity) VALUES (4, 'S', 25);
INSERT INTO product_stock (product_id, size, quantity) VALUES (4, 'M', 30);
INSERT INTO product_stock (product_id, size, quantity) VALUES (4, 'L', 10);

-- Product 5 stock: S:0 / M:1 / L:0
INSERT INTO product_stock (product_id, size, quantity) VALUES (5, 'S', 0);
INSERT INTO product_stock (product_id, size, quantity) VALUES (5, 'M', 1);
INSERT INTO product_stock (product_id, size, quantity) VALUES (5, 'L', 0);

-- Product 6 stock: S:9 / M:2 / L:5
INSERT INTO product_stock (product_id, size, quantity) VALUES (6, 'S', 9);
INSERT INTO product_stock (product_id, size, quantity) VALUES (6, 'M', 2);
INSERT INTO product_stock (product_id, size, quantity) VALUES (6, 'L', 5);
