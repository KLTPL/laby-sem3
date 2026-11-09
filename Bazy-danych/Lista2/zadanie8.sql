-- Zadanie 8
-- Uruchom jako root (fotograf nie ma prawa TRIGGER):
-- docker compose exec -T mysql mysql -uroot -proot -t aparaty < Lista2/zadanie8.sql
-- Test uprawnień w zadanie8.md.

DROP TRIGGER IF EXISTS dodaj_producenta;

DELIMITER //

-- BEFORE: producent musi istnieć, zanim MySQL sprawdzi klucz obcy.
CREATE TRIGGER dodaj_producenta
BEFORE INSERT ON Aparat
FOR EACH ROW
BEGIN
    DECLARE v_komunikat VARCHAR(128);

    IF NEW.producent IS NOT NULL
        AND NOT EXISTS (SELECT 1 FROM Producent WHERE ID = NEW.producent) THEN
        INSERT INTO Producent (ID, nazwa) VALUES (NEW.producent, 'nieznana');
    END IF;

    SET v_komunikat = CONCAT('Wstawiono nowy aparat: ', NEW.model);
    -- MySQL 8.4 nie przekazuje ostrzeżeń z wyzwalacza do klienta
    -- (SHOW WARNINGS jest puste), dlatego komunikat trafia też do @komunikat.
    SET @komunikat = v_komunikat;
    -- Klasa '01' to ostrzeżenie: INSERT wykonuje się dalej.
    SIGNAL SQLSTATE '01000' SET MESSAGE_TEXT = v_komunikat;
END //

DELIMITER ;
