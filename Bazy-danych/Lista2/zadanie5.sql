-- Zadanie 5
-- Uruchom jako root:
-- docker compose exec -T mysql mysql -uroot -proot -t aparaty < Lista2/zadanie5.sql

DROP PROCEDURE IF EXISTS najmniejsza_matryca;

DELIMITER //

-- Przy remisie zwraca model pierwszy alfabetycznie.
-- Producent bez aparatów => p_model = NULL.
CREATE PROCEDURE najmniejsza_matryca(IN p_producent INT, OUT p_model VARCHAR(30))
BEGIN
    SET p_model = (
        SELECT a.model
        FROM Aparat AS a
        INNER JOIN Matryca AS m ON a.matryca = m.ID
        WHERE a.producent = p_producent
        ORDER BY m.przekatna ASC, a.model ASC
        LIMIT 1
    );
END //

DELIMITER ;

-- Canon (ID 1): wynik procedury i dla porównania wszystkie jego aparaty.
CALL najmniejsza_matryca(1, @model);
SELECT @model AS model;

SELECT a.model, m.przekatna
FROM Aparat AS a
INNER JOIN Matryca AS m ON a.matryca = m.ID
WHERE a.producent = 1
ORDER BY m.przekatna ASC, a.model ASC;

-- Producent bez aparatów.
CALL najmniejsza_matryca(999, @model);
SELECT @model AS model;
