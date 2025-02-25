CREATE TABLE products (
    product_id  UUID PRIMARY KEY,
    name        TEXT NOT NULL,
    description TEXT NOT NULL,
    price       DECIMAL(10,2) NOT NULL,
    created_at  TIMESTAMP DEFAULT current_timestamp NOT NULL,
    updated_at  TIMESTAMP DEFAULT current_timestamp NOT NULL
);
