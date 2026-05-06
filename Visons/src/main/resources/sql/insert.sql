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