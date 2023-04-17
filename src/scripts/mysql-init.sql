DROP DATABASE IF EXISTS restdb;
DROP USER IF EXISTS `restadmin`@`%`;
create DATABASE IF NOT EXISTS restdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
create USER IF NOT EXISTS `restadmin`@`%` IDENTIFIED WITH mysql_native_password BY 'password';
GRANT select, insert, update, delete, create, DROP, REFERENCES, INDEX, ALTER, EXECUTE, create view, SHOW VIEW,
create ROUTINE, ALTER ROUTINE, EVENT, TRIGGER ON `restdb`.* TO `restadmin`@`%`;