USE Visons;
SET QUOTED_IDENTIFIER ON;

-- =====================================================
-- INSERTAR CATEGORÍAS (Necesarias para PRODUCTOS)
-- =====================================================
INSERT INTO CATEGORIES (name, description, is_active) 
VALUES 
('Frutas', 'Frutas frescas de temporada', 1),
('Verduras', 'Verduras y hortalizas', 1),
('Tubérculos', 'Papa, camote y similares', 1);

-- =====================================================
-- INSERTAR 5 PRODUCTOS
-- =====================================================
INSERT INTO PRODUCTS (category_id, name, variety, caliber, unit_measure, box_weight_kg, is_own_production, is_active, created_at)
VALUES 
(1, 'Manzana', 'Red Delicious', 'Grande', 'KG', 20.5, 0, 1, GETDATE()),
(1, 'Plátano', 'Cavendish', 'Mediano', 'KG', 18.0, 1, 1, GETDATE()),
(2, 'Lechuga', 'Iceberg', 'Standard', 'UNIT', 0.5, 1, 1, GETDATE()),
(2, 'Tomate', 'Cherry', 'Pequeño', 'KG', 15.0, 0, 1, GETDATE()),
(3, 'Papa', 'Blanca', 'Mediano', 'KG', 25.0, 1, 1, GETDATE());

-- =====================================================
-- INSERTAR 5 PROVEEDORES (PROVIDERS)
-- =====================================================
INSERT INTO PROVIDERS (company_name, tax_id, product_type, is_active)
VALUES 
('Agrícola del Norte', '20123456789', 'Frutas y Verduras', 1),
('Huertos Frescos', '20987654321', 'Productos Orgánicos', 1),
('Distribuidora Andina', '20111222333', 'Tubérculos y Raíces', 1),
('Finca San Luis', '20444555666', 'Frutas Tropicales', 1),
('Cooperativa El Sembrador', '20777888999', 'Verduras Frescas', 1);

-- =====================================================
-- INSERTAR 5 CLIENTES (CUSTOMERS/CLIENTS)
-- =====================================================
INSERT INTO CLIENTS (company_name, tax_id, country, address, email, credit_limit, is_active)
VALUES 
('Supermercado Plaza', '20123123123', 'Perú', 'Av. Principal 123, Lima', 'contacto@plaza.com', 50000.00, 1),
('Frutas Express', '20456456456', 'Perú', 'Calle Central 456, Arequipa', 'ventas@express.com', 25000.00, 1),
('Mercado Central', '20789789789', 'Perú', 'Jr. Comercio 789, Cusco', 'admin@mercado.com', 75000.00, 1),
('Verduras del Valle', '20321321321', 'Perú', 'Camino Real 321, Ayacucho', 'info@valle.com', 15000.00, 1),
('Distribuidora Regional', '20654654654', 'Perú', 'Ruta 5 654, Junín', 'contacto@regional.com', 100000.00, 1);

-- =====================================================
-- VERIFICAR INSERTS
-- =====================================================
SELECT 'PRODUCTOS' AS Tabla, COUNT(*) AS Total FROM PRODUCTS
UNION ALL
SELECT 'PROVEEDORES', COUNT(*) FROM PROVIDERS
UNION ALL
SELECT 'CLIENTES', COUNT(*) FROM CLIENTS;


