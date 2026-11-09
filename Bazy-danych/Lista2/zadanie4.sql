-- Zadanie 4
-- Tworzone i wywoływane jako fotograf, polecenia w zadanie4.md.

-- Bez DROP PROCEDURE IF EXISTS: gdy procedury jeszcze nie ma, fotograf
-- nie ma do niej ALTER ROUTINE i MySQL zwraca błąd 1370.

DELIMITER //

CREATE PROCEDURE generuj_aparaty()
BEGIN
    DECLARE dodane INT DEFAULT 0;
    DECLARE v_producent INT;
    DECLARE v_nazwa VARCHAR(50);
    DECLARE v_model VARCHAR(30);

    WHILE dodane < 100 DO
        -- Losowy producent, matryca i obiektyw spośród istniejących.
        SELECT ID, nazwa INTO v_producent, v_nazwa
        FROM Producent ORDER BY RAND() LIMIT 1;

        -- Nazwa: początek nazwy producenta + losowy kod, np. 'Nikon-3FA9C1'.
        SET v_model = CONCAT(
            LEFT(v_nazwa, 10), '-', UPPER(LEFT(MD5(RAND()), 6))
        );

        -- model to klucz główny: pomijamy wylosowany duplikat i losujemy dalej.
        IF NOT EXISTS (SELECT 1 FROM Aparat WHERE model = v_model) THEN
            INSERT INTO Aparat (model, producent, matryca, obiektyw, waga, typ)
            VALUES (
                v_model,
                v_producent,
                (SELECT ID FROM Matryca ORDER BY RAND() LIMIT 1),
                (SELECT ID FROM Obiektyw ORDER BY RAND() LIMIT 1),
                ROUND(150 + RAND() * 1350),
                ELT(1 + FLOOR(RAND() * 4),
                    'kompaktowy', 'lustrzanka', 'profesjonalny', 'inny')
            );
            SET dodane = dodane + 1;
        END IF;
    END WHILE;
END //

DELIMITER ;

CALL generuj_aparaty();

-- Czy modele się nie powtarzają? Zapytanie ma zwrócić 0 wierszy.
SELECT model, COUNT(*) AS ile
FROM Aparat
GROUP BY model
HAVING COUNT(*) > 1;

SELECT COUNT(*) AS aparaty FROM Aparat;

-- Odpowiedzi:
-- 1. Modele się nie powtarzają. model jest kluczem głównym, a procedura
--    przed wstawieniem sprawdza, czy taka nazwa już jest. Duplikat jest
--    pomijany i losowany jest następny, więc zawsze dochodzi dokładnie 100.
-- 2. Tak. fotograf ma CREATE ROUTINE, więc może utworzyć procedurę.
--    Twórca dostaje automatycznie EXECUTE i ALTER ROUTINE na swojej procedurze
--    (automatic_sp_privileges = ON). Procedura działa z prawami twórcy
--    (SQL SECURITY DEFINER), a fotograf ma SELECT i INSERT na tabelach.
SHOW GRANTS;
