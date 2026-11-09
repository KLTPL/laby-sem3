-- Zadanie 6
-- Uruchom jako root:
-- docker compose exec -T mysql mysql -uroot -proot -t aparaty < Lista2/zadanie6.sql

DROP FUNCTION IF EXISTS kategoria_jasnosci;

DELIMITER //

CREATE FUNCTION kategoria_jasnosci(id_obiektywu INT)
RETURNS VARCHAR(20)
READS SQL DATA
BEGIN
    -- Brak obiektywu => podzapytanie daje NULL.
    DECLARE v_przeslona FLOAT DEFAULT (
        SELECT minPrzeslona FROM Obiektyw WHERE ID = id_obiektywu
    );

    RETURN CASE
        WHEN v_przeslona IS NULL THEN NULL
        WHEN v_przeslona <= 1.8 THEN 'Jasny'
        WHEN v_przeslona <= 3.5 THEN 'Standardowy'
        ELSE 'Ciemny'
    END;
END //

DELIMITER ;

SELECT
    ID,
    model,
    minPrzeslona,
    kategoria_jasnosci(ID) AS jasnosc
FROM Obiektyw;

-- Nieistniejący obiektyw => NULL.
SELECT kategoria_jasnosci(999) AS jasnosc;
