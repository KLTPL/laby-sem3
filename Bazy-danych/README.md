# Bazy danych

## Useful MySQL commands

| Command | Description |
| --- | --- |
| `SHOW DATABASES;` | List all databases |
| `USE sakila;` | Select the database to work with |
| `SELECT DATABASE();` | Show the currently selected database |
| `SHOW TABLES;` | List all tables in the current database |
| `SHOW FULL TABLES WHERE Table_type = 'VIEW';` | List only views |
| `DESCRIBE actor;` | Show a table's columns, types, keys and defaults (alias: `DESC`) |
| `SHOW COLUMNS FROM actor;` | Same as `DESCRIBE` |
| `SHOW CREATE TABLE actor\G` | Show the full `CREATE TABLE` statement (schema) |
| `SHOW INDEX FROM actor;` | List a table's indexes |
| `SHOW TABLE STATUS LIKE 'actor'\G` | Show engine, row count, size and other table metadata |
| `SHOW TABLES LIKE 'film%';` | List tables matching a pattern |
| `SHOW CREATE VIEW film_list\G` | Show a view's definition |
| `SHOW TRIGGERS;` | List triggers in the current database |
| `SHOW PROCEDURE STATUS WHERE Db = 'sakila';` | List stored procedures |
| `SHOW FUNCTION STATUS WHERE Db = 'sakila';` | List stored functions |
| `SELECT * FROM information_schema.KEY_COLUMN_USAGE WHERE TABLE_SCHEMA = 'sakila' AND TABLE_NAME = 'film_actor';` | Show foreign keys of a table |
| `EXPLAIN SELECT * FROM actor WHERE actor_id = 1;` | Show the query execution plan |
| `SHOW WARNINGS;` | Show warnings from the last statement |
| `SHOW PROCESSLIST;` | Show currently running connections and queries |
| `SELECT VERSION();` | Show the server version |
| `SOURCE file.sql;` | Execute an SQL script from within the client |
| `STATUS;` | Show connection and server info (client command) |
| `HELP SHOW;` | Show help for `SHOW` statements |
| `EXIT;` | Leave the client (alias: `QUIT`, `\q`) |

Tip: end a statement with `\G` instead of `;` to print the result vertically, which is easier to read for wide rows.
