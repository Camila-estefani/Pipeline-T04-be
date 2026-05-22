-- Migracion segura para una base de datos existente.
-- Ejecutar este archivo en lugar de db.sql/completo.sql si las tablas ya existen.
-- No elimina datos: solo agrega la columna country e indices faltantes.
-- No usa variables ni GO, para poder ejecutarse como un solo script en DBeaver/SSMS.

IF OBJECT_ID(N'CLIENT_REQUESTS', N'U') IS NOT NULL
   AND COL_LENGTH(N'CLIENT_REQUESTS', N'country') IS NULL
BEGIN
    EXEC(N'ALTER TABLE CLIENT_REQUESTS ADD country NVARCHAR(100) NULL');
END;

IF OBJECT_ID(N'CLIENT_REQUESTS', N'U') IS NOT NULL
   AND COL_LENGTH(N'CLIENT_REQUESTS', N'country') IS NOT NULL
BEGIN
    EXEC(N'UPDATE CLIENT_REQUESTS
           SET country = N''Peru''
           WHERE country IS NULL OR LTRIM(RTRIM(country)) = N''''');
END;

IF OBJECT_ID(N'PRODUCTS', N'U') IS NOT NULL
   AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'idx_products_name' AND object_id = OBJECT_ID(N'PRODUCTS'))
BEGIN
    CREATE INDEX idx_products_name ON PRODUCTS(name);
END;

IF OBJECT_ID(N'PRODUCTS', N'U') IS NOT NULL
   AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'idx_products_state' AND object_id = OBJECT_ID(N'PRODUCTS'))
BEGIN
    CREATE INDEX idx_products_state ON PRODUCTS(is_active);
END;

IF OBJECT_ID(N'PRODUCTS', N'U') IS NOT NULL
   AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'idx_products_category_state' AND object_id = OBJECT_ID(N'PRODUCTS'))
BEGIN
    CREATE INDEX idx_products_category_state ON PRODUCTS(category_id, is_active);
END;

IF OBJECT_ID(N'PURCHASES', N'U') IS NOT NULL
   AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'idx_purchases_provider' AND object_id = OBJECT_ID(N'PURCHASES'))
BEGIN
    CREATE INDEX idx_purchases_provider ON PURCHASES(provider_id);
END;

IF OBJECT_ID(N'CLIENTS', N'U') IS NOT NULL
   AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'idx_clients_company_name' AND object_id = OBJECT_ID(N'CLIENTS'))
BEGIN
    CREATE INDEX idx_clients_company_name ON CLIENTS(company_name);
END;

IF OBJECT_ID(N'ORDERS', N'U') IS NOT NULL
   AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'idx_orders_client' AND object_id = OBJECT_ID(N'ORDERS'))
BEGIN
    CREATE INDEX idx_orders_client ON ORDERS(client_id);
END;

IF OBJECT_ID(N'ORDERS', N'U') IS NOT NULL
   AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'idx_orders_client_date' AND object_id = OBJECT_ID(N'ORDERS'))
BEGIN
    CREATE INDEX idx_orders_client_date ON ORDERS(client_id, order_date DESC);
END;

IF OBJECT_ID(N'ORDERS', N'U') IS NOT NULL
   AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'idx_orders_status_date' AND object_id = OBJECT_ID(N'ORDERS'))
BEGIN
    CREATE INDEX idx_orders_status_date ON ORDERS(status, order_date DESC);
END;

IF OBJECT_ID(N'ORDERS', N'U') IS NOT NULL
   AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'idx_orders_date' AND object_id = OBJECT_ID(N'ORDERS'))
BEGIN
    CREATE INDEX idx_orders_date ON ORDERS(order_date DESC);
END;

IF OBJECT_ID(N'ORDER_DETAILS', N'U') IS NOT NULL
   AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'idx_order_details_order' AND object_id = OBJECT_ID(N'ORDER_DETAILS'))
BEGIN
    CREATE INDEX idx_order_details_order ON ORDER_DETAILS(order_id);
END;

IF OBJECT_ID(N'ORDER_DETAILS', N'U') IS NOT NULL
   AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'idx_order_details_product' AND object_id = OBJECT_ID(N'ORDER_DETAILS'))
BEGIN
    CREATE INDEX idx_order_details_product ON ORDER_DETAILS(product_id);
END;

IF OBJECT_ID(N'CLIENT_REQUESTS', N'U') IS NOT NULL
   AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = N'idx_client_requests_status_date' AND object_id = OBJECT_ID(N'CLIENT_REQUESTS'))
BEGIN
    CREATE INDEX idx_client_requests_status_date ON CLIENT_REQUESTS(status, request_date DESC);
END;
