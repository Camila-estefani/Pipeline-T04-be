CREATE DATABASE Visons;
GO

USE Visons;
GO

CREATE TABLE CATEGORIES (
    category_id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    description NVARCHAR(255) NULL,
    is_active BIT NOT NULL DEFAULT 1
);

CREATE TABLE PRODUCTS (
    product_id INT IDENTITY(1,1) PRIMARY KEY,
    category_id INT NOT NULL FOREIGN KEY REFERENCES CATEGORIES(category_id),
    name NVARCHAR(150) NOT NULL,
    variety NVARCHAR(100) NULL,
    caliber NVARCHAR(50) NULL,
    unit_measure NVARCHAR(20) NOT NULL DEFAULT 'KG',
    box_weight_kg DECIMAL(10,2) NULL,
    is_own_production BIT NOT NULL DEFAULT 0,
    is_active BIT NOT NULL DEFAULT 1,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    deleted_at DATETIME NULL,
    restored_at DATETIME NULL
);

CREATE TABLE PROVIDERS (
    provider_id INT IDENTITY(1,1) PRIMARY KEY,
    company_name NVARCHAR(200) NOT NULL,
    tax_id NVARCHAR(20) NULL UNIQUE,
    product_type NVARCHAR(100) NULL,
    is_active BIT NOT NULL DEFAULT 1
);

CREATE TABLE CLIENTS (
    client_id INT IDENTITY(1,1) PRIMARY KEY,
    company_name NVARCHAR(200) NOT NULL,
    tax_id NVARCHAR(20) NOT NULL UNIQUE,
    country NVARCHAR(100) NULL,
    address NVARCHAR(MAX) NULL,
    email NVARCHAR(150) NULL,
    credit_limit DECIMAL(18,2) NULL,
    is_active BIT NOT NULL DEFAULT 1
);

CREATE TABLE UBIGEO (
    ubigeo_id INT IDENTITY(1,1) PRIMARY KEY,
    ubigeo_code CHAR(6) NOT NULL UNIQUE,
    department NVARCHAR(100) NOT NULL,
    province NVARCHAR(100) NOT NULL,
    district NVARCHAR(100) NOT NULL,
    is_active BIT NOT NULL DEFAULT 1
);

CREATE TABLE CLIENT_REQUESTS (
    request_id INT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(50) NOT NULL UNIQUE,
    first_name NVARCHAR(100) NOT NULL,
    last_name NVARCHAR(100) NOT NULL,
    company_name NVARCHAR(200) NULL,
    tax_id NVARCHAR(20) NULL,
    email NVARCHAR(150) NOT NULL,
    phone NVARCHAR(20) NULL,
    address NVARCHAR(MAX) NULL,
    ubigeo_id INT NULL FOREIGN KEY REFERENCES UBIGEO(ubigeo_id),
    status NVARCHAR(50) NOT NULL DEFAULT 'Pending',
    request_date DATETIME NOT NULL DEFAULT GETDATE(),
    reviewed_by INT NULL,
    comments NVARCHAR(MAX) NULL
);

CREATE TABLE BATCHES (
    batch_id INT IDENTITY(1,1) PRIMARY KEY,
    product_id INT NOT NULL FOREIGN KEY REFERENCES PRODUCTS(product_id),
    provider_id INT NULL FOREIGN KEY REFERENCES PROVIDERS(provider_id),
    batch_code NVARCHAR(50) NOT NULL UNIQUE,
    harvest_date DATE NULL,
    available_quantity_kg DECIMAL(18,3) NULL,
    origin_farm NVARCHAR(200) NULL
);

CREATE TABLE CURRENT_INVENTORY (
    inventory_id INT IDENTITY(1,1) PRIMARY KEY,
    product_id INT NOT NULL FOREIGN KEY REFERENCES PRODUCTS(product_id),
    total_stock_kg DECIMAL(18,3) NOT NULL DEFAULT 0,
    reserved_stock_kg DECIMAL(18,3) NOT NULL DEFAULT 0,
    available_stock_kg DECIMAL(18,3) NOT NULL DEFAULT 0
);

CREATE TABLE PURCHASES (
    purchase_id INT IDENTITY(1,1) PRIMARY KEY,
    provider_id INT NOT NULL FOREIGN KEY REFERENCES PROVIDERS(provider_id),
    order_code NVARCHAR(50) NULL UNIQUE,
    total_amount DECIMAL(18,2) NULL,
    status NVARCHAR(50) NULL,
    purchase_date DATE NOT NULL DEFAULT GETDATE()
);

CREATE TABLE PURCHASE_DETAILS (
    detail_id INT IDENTITY(1,1) PRIMARY KEY,
    purchase_id INT NOT NULL FOREIGN KEY REFERENCES PURCHASES(purchase_id),
    product_id INT NOT NULL FOREIGN KEY REFERENCES PRODUCTS(product_id),
    quantity_kg DECIMAL(18,3) NOT NULL,
    unit_price DECIMAL(18,4) NOT NULL
);

CREATE TABLE ORDERS (
    order_id INT IDENTITY(1,1) PRIMARY KEY,
    client_id INT NOT NULL FOREIGN KEY REFERENCES CLIENTS(client_id),
    order_code NVARCHAR(50) NULL UNIQUE,
    order_date DATE NOT NULL DEFAULT GETDATE(),
    incoterm CHAR(3) NULL,
    status NVARCHAR(50) NULL
);

CREATE TABLE ORDER_DETAILS (
    order_detail_id INT IDENTITY(1,1) PRIMARY KEY,
    order_id INT NOT NULL FOREIGN KEY REFERENCES ORDERS(order_id),
    product_id INT NOT NULL FOREIGN KEY REFERENCES PRODUCTS(product_id),
    batch_id INT NULL FOREIGN KEY REFERENCES BATCHES(batch_id),
    quantity_kg DECIMAL(18,3) NOT NULL,
    unit_price DECIMAL(18,4) NOT NULL
);



