-- Zadanie 2
-- Uruchom jako root (fotograf nie ma prawa CREATE):
-- docker compose exec -T mysql mysql -uroot -proot -t aparaty < Lista2/zadanie2.sql

CREATE TABLE Producent (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    nazwa VARCHAR(50) NOT NULL,
    -- brak danych => 'nieznany'
    kraj VARCHAR(20) NOT NULL DEFAULT 'nieznany',
    adresKorespondencyjny VARCHAR(100) NOT NULL DEFAULT 'nieznany',
    CONSTRAINT producent_nazwa_niepusta CHECK (TRIM(nazwa) <> '')
);

-- Numeracja od 100.
-- MySQL nie pozwala na CHECK dla kolumny AUTO_INCREMENT (błąd 3818),
-- więc ID pilnuje sam licznik.
CREATE TABLE Matryca (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    przekatna DECIMAL(4, 2),
    rozdzielczosc DECIMAL(3, 1),
    typ VARCHAR(10),
    CONSTRAINT matryca_przekatna_nieujemna CHECK (przekatna >= 0),
    CONSTRAINT matryca_rozdzielczosc_nieujemna CHECK (rozdzielczosc >= 0)
) AUTO_INCREMENT = 100;

CREATE TABLE Obiektyw (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    model VARCHAR(30),
    minPrzeslona FLOAT,
    maxPrzeslona FLOAT,
    CONSTRAINT obiektyw_min_nieujemna CHECK (minPrzeslona >= 0),
    CONSTRAINT obiektyw_max_nieujemna CHECK (maxPrzeslona >= 0),
    CONSTRAINT obiektyw_min_mniejsza CHECK (minPrzeslona < maxPrzeslona)
);

-- Klucz główny to model (VARCHAR), więc bez AUTO_INCREMENT.
-- Kolumny z kluczami obcymi są nieujemne, bo muszą wskazywać istniejące ID.
CREATE TABLE Aparat (
    model VARCHAR(30) PRIMARY KEY,
    producent INT,
    matryca INT,
    obiektyw INT,
    waga FLOAT,
    typ ENUM('kompaktowy', 'lustrzanka', 'profesjonalny', 'inny'),
    CONSTRAINT aparat_waga_nieujemna CHECK (waga >= 0),
    CONSTRAINT aparat_producent_fk FOREIGN KEY (producent)
    REFERENCES Producent (ID),
    CONSTRAINT aparat_matryca_fk FOREIGN KEY (matryca)
    REFERENCES Matryca (ID),
    CONSTRAINT aparat_obiektyw_fk FOREIGN KEY (obiektyw)
    REFERENCES Obiektyw (ID)
);

SHOW TABLES;
