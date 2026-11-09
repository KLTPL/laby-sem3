-- Zadanie 1
-- Uruchom jako root (z katalogu Bazy-danych):
-- docker compose exec -T mysql mysql -uroot -proot -t < Lista2/zadanie1.sql

CREATE DATABASE IF NOT EXISTS aparaty;

CREATE USER IF NOT EXISTS 'fotograf'@'%' IDENTIFIED BY 'Fotograf123!';

-- SELECT, INSERT, UPDATE na danych + tworzenie procedur.
-- Bez CREATE, DROP, ALTER, więc nie zmieni struktury tabel.
GRANT SELECT, INSERT, UPDATE, CREATE ROUTINE ON aparaty.* TO 'fotograf'@'%';
SHOW GRANTS FOR 'fotograf'@'%';

-- Odbieramy UPDATE...
REVOKE UPDATE ON aparaty.* FROM 'fotograf'@'%';
SHOW GRANTS FOR 'fotograf'@'%';

-- ...i nadajemy ponownie.
GRANT UPDATE ON aparaty.* TO 'fotograf'@'%';
SHOW GRANTS FOR 'fotograf'@'%';
