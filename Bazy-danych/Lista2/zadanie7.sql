-- Zadanie 7
-- Uruchom jako root (fotograf nie ma prawa TRIGGER ani DELETE):
-- docker compose exec -T mysql mysql -uroot -proot -t aparaty < Lista2/zadanie7.sql

DROP TRIGGER IF EXISTS usun_nieuzywana_matryce;

DELIMITER //

-- AFTER: usunięty aparat już nie istnieje, więc sprawdzamy pozostałe.
CREATE TRIGGER usun_nieuzywana_matryce
AFTER DELETE ON Aparat
FOR EACH ROW
BEGIN
    IF NOT EXISTS (SELECT 1 FROM Aparat WHERE matryca = OLD.matryca) THEN
        DELETE FROM Matryca WHERE ID = OLD.matryca;
    END IF;
END //

DELIMITER ;

-- Test w transakcji, na koniec ROLLBACK przywraca dane.
START TRANSACTION;

-- Nowa matryca używana przez dwa aparaty.
INSERT INTO Matryca (przekatna, rozdzielczosc, typ) VALUES (11.00, 8.0, 'CCD');
SET @m = LAST_INSERT_ID();
INSERT INTO Aparat (model, producent, matryca, obiektyw, waga, typ) VALUES
('Test-1', 1, @m, 1, 300, 'kompaktowy'),
('Test-2', 1, @m, 1, 300, 'kompaktowy');

-- Nie ostatni aparat => matryca zostaje (1 wiersz).
DELETE FROM Aparat WHERE model = 'Test-1';
SELECT * FROM Matryca WHERE ID = @m;

-- Ostatni aparat => matryca usunięta (0 wierszy).
DELETE FROM Aparat WHERE model = 'Test-2';
SELECT * FROM Matryca WHERE ID = @m;

ROLLBACK;
