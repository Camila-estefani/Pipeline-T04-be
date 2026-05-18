SET NOCOUNT ON;
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO

UPDATE WORKERS SET email='luis@empresa.com', phone='999111111' WHERE document_number='70000001';
UPDATE WORKERS SET email='ana@empresa.com', phone='999111122' WHERE document_number='70000002';
UPDATE WORKERS SET email='carlos@empresa.com', phone='999111113' WHERE document_number='70000003';
UPDATE WORKERS SET email='maria@empresa.com', phone='999111114' WHERE document_number='70000004';
UPDATE WORKERS SET email='jose@empresa.com', phone='999111115' WHERE document_number='70000005';
UPDATE WORKERS SET email='elena@empresa.com', phone='999111116' WHERE document_number='70000006';
UPDATE WORKERS SET email='pedro@empresa.com', phone='999111117' WHERE document_number='70000007';
UPDATE WORKERS SET email='lucia@empresa.com', phone='999111118' WHERE document_number='70000008';
UPDATE WORKERS SET email='diego@empresa.com', phone='999111119' WHERE document_number='70000009';
UPDATE WORKERS SET email='sofia@empresa.com', phone='999111120' WHERE document_number='70000010';
UPDATE CLIENTS SET company_name='AgroExport S.A.C.', tax_id='20601234567', email='cliente@agroexport.pe' WHERE client_id=3;

UPDATE USERS
SET username='luis@empresa.com',
    password_hash='70000001',
    user_type_id=(SELECT id FROM USER_TYPES WHERE name='ADMIN'),
    worker_id=1,
    client_id=NULL,
    is_active=1
WHERE user_id=1;

IF NOT EXISTS (SELECT 1 FROM USERS WHERE username='ana@empresa.com')
INSERT INTO USERS (username,password_hash,user_type_id,worker_id,client_id,is_active)
VALUES ('ana@empresa.com','70000002',(SELECT id FROM USER_TYPES WHERE name='EMPLOYEE'),4,NULL,1);

IF NOT EXISTS (SELECT 1 FROM USERS WHERE username='carlos@empresa.com')
INSERT INTO USERS (username,password_hash,user_type_id,worker_id,client_id,is_active)
VALUES ('carlos@empresa.com','70000003',(SELECT id FROM USER_TYPES WHERE name='EMPLOYEE'),5,NULL,1);

IF NOT EXISTS (SELECT 1 FROM USERS WHERE username='maria@empresa.com')
INSERT INTO USERS (username,password_hash,user_type_id,worker_id,client_id,is_active)
VALUES ('maria@empresa.com','70000004',(SELECT id FROM USER_TYPES WHERE name='EMPLOYEE'),6,NULL,1);

IF NOT EXISTS (SELECT 1 FROM USERS WHERE username='jose@empresa.com')
INSERT INTO USERS (username,password_hash,user_type_id,worker_id,client_id,is_active)
VALUES ('jose@empresa.com','70000005',(SELECT id FROM USER_TYPES WHERE name='EMPLOYEE'),7,NULL,1);

IF NOT EXISTS (SELECT 1 FROM USERS WHERE username='elena@empresa.com')
INSERT INTO USERS (username,password_hash,user_type_id,worker_id,client_id,is_active)
VALUES ('elena@empresa.com','70000006',(SELECT id FROM USER_TYPES WHERE name='EMPLOYEE'),8,NULL,1);

IF NOT EXISTS (SELECT 1 FROM USERS WHERE username='pedro@empresa.com')
INSERT INTO USERS (username,password_hash,user_type_id,worker_id,client_id,is_active)
VALUES ('pedro@empresa.com','70000007',(SELECT id FROM USER_TYPES WHERE name='EMPLOYEE'),9,NULL,1);

IF NOT EXISTS (SELECT 1 FROM USERS WHERE username='lucia@empresa.com')
INSERT INTO USERS (username,password_hash,user_type_id,worker_id,client_id,is_active)
VALUES ('lucia@empresa.com','70000008',(SELECT id FROM USER_TYPES WHERE name='EMPLOYEE'),10,NULL,1);

IF NOT EXISTS (SELECT 1 FROM USERS WHERE username='diego@empresa.com')
INSERT INTO USERS (username,password_hash,user_type_id,worker_id,client_id,is_active)
VALUES ('diego@empresa.com','70000009',(SELECT id FROM USER_TYPES WHERE name='EMPLOYEE'),11,NULL,1);

IF NOT EXISTS (SELECT 1 FROM USERS WHERE username='sofia@empresa.com')
INSERT INTO USERS (username,password_hash,user_type_id,worker_id,client_id,is_active)
VALUES ('sofia@empresa.com','70000010',(SELECT id FROM USER_TYPES WHERE name='EMPLOYEE'),12,NULL,1);

IF NOT EXISTS (SELECT 1 FROM USERS WHERE username='cliente@agroexport.pe')
INSERT INTO USERS (username,password_hash,user_type_id,worker_id,client_id,is_active)
VALUES ('cliente@agroexport.pe','20601234567',(SELECT id FROM USER_TYPES WHERE name='CLIENT'),NULL,3,1);

DELETE FROM USER_ROLES;
INSERT INTO USER_ROLES (user_id, role_id)
VALUES
((SELECT user_id FROM USERS WHERE username='luis@empresa.com'), (SELECT role_id FROM ROLES WHERE name='ADMIN')),
((SELECT user_id FROM USERS WHERE username='ana@empresa.com'), (SELECT role_id FROM ROLES WHERE name='EMPLOYEE')),
((SELECT user_id FROM USERS WHERE username='carlos@empresa.com'), (SELECT role_id FROM ROLES WHERE name='EMPLOYEE')),
((SELECT user_id FROM USERS WHERE username='maria@empresa.com'), (SELECT role_id FROM ROLES WHERE name='EMPLOYEE')),
((SELECT user_id FROM USERS WHERE username='jose@empresa.com'), (SELECT role_id FROM ROLES WHERE name='EMPLOYEE')),
((SELECT user_id FROM USERS WHERE username='elena@empresa.com'), (SELECT role_id FROM ROLES WHERE name='EMPLOYEE')),
((SELECT user_id FROM USERS WHERE username='pedro@empresa.com'), (SELECT role_id FROM ROLES WHERE name='EMPLOYEE')),
((SELECT user_id FROM USERS WHERE username='lucia@empresa.com'), (SELECT role_id FROM ROLES WHERE name='EMPLOYEE')),
((SELECT user_id FROM USERS WHERE username='diego@empresa.com'), (SELECT role_id FROM ROLES WHERE name='EMPLOYEE')),
((SELECT user_id FROM USERS WHERE username='sofia@empresa.com'), (SELECT role_id FROM ROLES WHERE name='EMPLOYEE')),
((SELECT user_id FROM USERS WHERE username='cliente@agroexport.pe'), (SELECT role_id FROM ROLES WHERE name='CLIENT'));
GO

SELECT user_id, username, password_hash, worker_id, client_id FROM USERS ORDER BY user_id;
GO