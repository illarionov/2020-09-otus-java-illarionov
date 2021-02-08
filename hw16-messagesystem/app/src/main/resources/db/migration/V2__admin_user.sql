INSERT INTO address VALUES (1, 'ул. Петрова');
INSERT INTO user_ VALUES (1, 20, 'admin', 'admin', 'Admin', 'ADMIN', 1);
INSERT INTO phone VALUES (1, '12-45-77',  1);
INSERT INTO phone VALUES (2, '34-56-87',  1);

SELECT pg_catalog.setval(pg_get_serial_sequence('address', 'id'), MAX(id)) FROM address;
SELECT pg_catalog.setval(pg_get_serial_sequence('user_', 'id'), MAX(id)) FROM user_;
SELECT pg_catalog.setval(pg_get_serial_sequence('phone', 'id'), MAX(id)) FROM phone;