CREATE TABLE products (
    technical_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    business_id VARCHAR(50) UNIQUE NOT NULL,
    product_code VARCHAR(10) NOT NULL,
    product_name VARCHAR(200) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    validity_indicator BOOLEAN DEFAULT TRUE,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_auditor VARCHAR(100) NOT NULL
);
CREATE INDEX idx_prod_search ON products (product_name, product_code, price);