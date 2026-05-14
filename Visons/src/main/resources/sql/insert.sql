USE Visons;
GO

SET NOCOUNT ON;
GO

--------------------------------------------------
-- UBIGEO (10)
--------------------------------------------------
INSERT INTO UBIGEO (ubigeo_code, department, province, district)
VALUES
('150101','Lima','Lima','Cercado'),
('150102','Lima','Lima','Ate'),
('150103','Lima','Lima','Surco'),
('040101','Arequipa','Arequipa','Cercado'),
('080101','Cusco','Cusco','Cusco'),
('120101','Junín','Huancayo','Huancayo'),
('050101','Ayacucho','Huamanga','Ayacucho'),
('130101','La Libertad','Trujillo','Trujillo'),
('200101','Piura','Piura','Piura'),
('210101','Puno','Puno','Puno');
GO

--------------------------------------------------
-- USER_TYPES
--------------------------------------------------
INSERT INTO USER_TYPES (name)
VALUES ('ADMIN'),('EMPLOYEE'),('CLIENT');
GO

--------------------------------------------------
-- WORKERS (CORREGIDO - usa UBIGEO REAL)
--------------------------------------------------
INSERT INTO WORKERS (first_name,last_name,phone,email,address,ubigeo_id,document_type,document_number,hire_date)
VALUES
('Luis','Perez','999111111','luis@empresa.com','Lima',
 (SELECT ubigeo_id FROM UBIGEO WHERE ubigeo_code='150101'),
'DNI','70000001',GETDATE()),

('Ana','Lopez','999111112','ana@empresa.com','Lima',
 (SELECT ubigeo_id FROM UBIGEO WHERE ubigeo_code='150102'),
'DNI','70000002',GETDATE()),

('Carlos','Diaz','999111113','carlos@empresa.com','Lima',
 (SELECT ubigeo_id FROM UBIGEO WHERE ubigeo_code='150103'),
'DNI','70000003',GETDATE()),

('Maria','Torres','999111114','maria@empresa.com','Arequipa',
 (SELECT ubigeo_id FROM UBIGEO WHERE ubigeo_code='040101'),
'DNI','70000004',GETDATE()),

('Jose','Ramos','999111115','jose@empresa.com','Cusco',
 (SELECT ubigeo_id FROM UBIGEO WHERE ubigeo_code='080101'),
'DNI','70000005',GETDATE()),

('Elena','Vega','999111116','elena@empresa.com','Junin',
 (SELECT ubigeo_id FROM UBIGEO WHERE ubigeo_code='120101'),
'DNI','70000006',GETDATE()),

('Pedro','Castro','999111117','pedro@empresa.com','Ayacucho',
 (SELECT ubigeo_id FROM UBIGEO WHERE ubigeo_code='050101'),
'DNI','70000007',GETDATE()),

('Lucia','Flores','999111118','lucia@empresa.com','Trujillo',
 (SELECT ubigeo_id FROM UBIGEO WHERE ubigeo_code='130101'),
'DNI','70000008',GETDATE()),

('Diego','Mendoza','999111119','diego@empresa.com','Piura',
 (SELECT ubigeo_id FROM UBIGEO WHERE ubigeo_code='200101'),
'DNI','70000009',GETDATE()),

('Sofia','Reyes','999111120','sofia@empresa.com','Puno',
 (SELECT ubigeo_id FROM UBIGEO WHERE ubigeo_code='210101'),
'DNI','70000010',GETDATE());
GO

--------------------------------------------------
-- USERS (CORREGIDO)
--------------------------------------------------
INSERT INTO USERS (username,password_hash,user_type_id,worker_id,is_active)
VALUES
('admin','123',
 (SELECT id FROM USER_TYPES WHERE name='ADMIN'),
 1,1),

('emp1','123',
 (SELECT id FROM USER_TYPES WHERE name='EMPLOYEE'),
 2,1),

('emp2','123',
 (SELECT id FROM USER_TYPES WHERE name='EMPLOYEE'),
 3,1),

('emp3','123',
 (SELECT id FROM USER_TYPES WHERE name='EMPLOYEE'),
 4,1),

('emp4','123',
 (SELECT id FROM USER_TYPES WHERE name='EMPLOYEE'),
 5,1),

('emp5','123',
 (SELECT id FROM USER_TYPES WHERE name='EMPLOYEE'),
 6,1),

('emp6','123',
 (SELECT id FROM USER_TYPES WHERE name='EMPLOYEE'),
 7,1),

('emp7','123',
 (SELECT id FROM USER_TYPES WHERE name='EMPLOYEE'),
 8,1),

('emp8','123',
 (SELECT id FROM USER_TYPES WHERE name='EMPLOYEE'),
 9,1),

('emp9','123',
 (SELECT id FROM USER_TYPES WHERE name='EMPLOYEE'),
 10,1);
GO

--------------------------------------------------
-- ROLES
--------------------------------------------------

INSERT INTO ROLES (name, description)
VALUES
('ADMIN', 'Administrador del sistema'),
('EMPLOYEE', 'Empleado del sistema'),
('CLIENT', 'Cliente del sistema');
GO
--------------------------------------------------
-- USER_ROLES (CORREGIDO)
--------------------------------------------------
INSERT INTO USER_ROLES (user_id,role_id)
VALUES
((SELECT user_id FROM USERS WHERE username='admin'),
 (SELECT role_id FROM ROLES WHERE name='ADMIN')),

((SELECT user_id FROM USERS WHERE username='emp1'),
 (SELECT role_id FROM ROLES WHERE name='EMPLOYEE')),

((SELECT user_id FROM USERS WHERE username='emp2'),
 (SELECT role_id FROM ROLES WHERE name='EMPLOYEE'));
GO

-- =========================================
-- TABLA: CATEGORIES
-- Necesaria para relacionar los productos
-- =========================================

INSERT INTO CATEGORIES (name, description, is_active)
VALUES
('Frutas', 'Productos frutales frescos', 1),
('Verduras', 'Productos vegetales frescos', 1),
('Exportacion', 'Productos para exportacion', 1),
('Organicos', 'Productos organicos certificados', 1),
('Citricos', 'Frutas citricas', 1),
('Tropicales', 'Frutas tropicales', 1),
('Congelados', 'Productos congelados', 1),
('Premium', 'Productos premium seleccionados', 1),
('Procesados', 'Productos procesados', 1),
('Agroindustria', 'Productos agroindustriales', 1);

-- =========================================
-- TABLA: PROVIDERS
-- Necesaria para registrar compras
-- =========================================

INSERT INTO PROVIDERS (
    company_name,
    tax_id,
    product_type,
    is_active,
    created_at
)
VALUES
('AgroExport SAC', '20111111111', 'Mangos', 1, GETDATE()),
('Campos del Sur SAC', '20222222222', 'Palta', 1, GETDATE()),
('Fresh Fruits Peru', '20333333333', 'Uvas', 1, GETDATE()),
('Green Valley SAC', '20444444444', 'Citricos', 1, GETDATE()),
('Vision Agro SAC', '20555555555', 'Banano', 1, GETDATE()),
('Natural Foods SAC', '20666666666', 'Arandanos', 1, GETDATE()),
('Exportadora Norte', '20777777777', 'Limon', 1, GETDATE()),
('Peru Fresh Company', '20888888888', 'Papaya', 1, GETDATE()),
('BioCampos SAC', '20999999999', 'Organicos', 1, GETDATE()),
('Sun Fruits Peru', '20101010101', 'Piña', 1, GETDATE());

-- =========================================
-- TABLA MAESTRA: PRODUCTS
-- 10 inserts de productos
-- =========================================

INSERT INTO PRODUCTS (
    category_id,
    name,
    variety,
    caliber,
    unit_measure,
    box_weight_kg,
    is_own_production,
    is_active,
    created_at
)
VALUES
(1, 'Mango', 'Kent', 'Grande', 'KG', 10.50, 1, 1, GETDATE()),
(1, 'Palta', 'Hass', 'Mediano', 'KG', 8.00, 1, 1, GETDATE()),
(5, 'Limon', 'Tahiti', 'Pequeño', 'KG', 12.00, 0, 1, GETDATE()),
(6, 'Papaya', 'Maradol', 'Grande', 'KG', 15.00, 0, 1, GETDATE()),
(6, 'Piña', 'Golden', 'Grande', 'KG', 14.00, 1, 1, GETDATE()),
(1, 'Uva', 'Red Globe', 'Mediano', 'KG', 9.50, 1, 1, GETDATE()),
(4, 'Arandano', 'Bluecrop', 'Pequeño', 'KG', 5.00, 0, 1, GETDATE()),
(5, 'Naranja', 'Valencia', 'Grande', 'KG', 13.00, 1, 1, GETDATE()),
(2, 'Espinaca', 'Baby', 'Pequeño', 'KG', 4.00, 0, 1, GETDATE()),
(8, 'Fresa', 'Premium', 'Mediano', 'KG', 6.50, 1, 1, GETDATE());

-- =========================================
-- TABLA TRANSACCIONAL: PURCHASES
-- Registro de compras realizadas
-- =========================================

INSERT INTO PURCHASES (
    provider_id,
    order_code,
    total_amount,
    status,
    purchase_date
)
VALUES
(1, 'PUR-001', 2500.00, 'COMPLETED', GETDATE()),
(2, 'PUR-002', 1800.50, 'COMPLETED', GETDATE()),
(3, 'PUR-003', 3200.75, 'PENDING', GETDATE()),
(4, 'PUR-004', 4100.20, 'COMPLETED', GETDATE()),
(5, 'PUR-005', 950.00, 'PENDING', GETDATE()),
(6, 'PUR-006', 2750.40, 'COMPLETED', GETDATE()),
(7, 'PUR-007', 3890.00, 'COMPLETED', GETDATE()),
(8, 'PUR-008', 1450.90, 'PENDING', GETDATE()),
(9, 'PUR-009', 5200.00, 'COMPLETED', GETDATE()),
(10, 'PUR-010', 1999.99, 'COMPLETED', GETDATE());

-- =========================================
-- TABLA TRANSACCIONAL: PURCHASE_DETAILS
-- Detalle de productos comprados
-- =========================================

INSERT INTO PURCHASE_DETAILS (
    purchase_id,
    product_id,
    quantity_kg,
    unit_price
)
VALUES
(1, 1, 500.000, 5.0000),
(2, 2, 300.000, 6.0016),
(3, 3, 700.000, 4.5725),
(4, 4, 1000.000, 4.1002),
(5, 5, 200.000, 4.7500),
(6, 6, 450.000, 6.1120),
(7, 7, 800.000, 4.8625),
(8, 8, 250.000, 5.8036),
(9, 9, 1200.000, 4.3333),
(10, 10, 350.000, 5.7142);






--Consulta beneficiada Tabla Products--
SELECT *
FROM PRODUCTS
WHERE name = 'Mango';

--Consulta beneficiada Tabla PURCHASES--
SELECT *
FROM PURCHASES
WHERE provider_id = 3;
