-- Zadanie 3
-- Uruchamiane jako użytkownik fotograf, polecenie w zadanie3.md.

-- Ostatnie 3 rekordy bez kraju/adresu => 'nieznany' (DEFAULT z zad. 2).
INSERT INTO Producent (nazwa, kraj, adresKorespondencyjny) VALUES
('Canon', 'Japonia', 'Tokio, Shimomaruko 3-30-2'),
('Nikon', 'Japonia', 'Tokio, Shinagawa 1-5-20'),
('Sony', 'Japonia', 'Tokio, Konan 1-7-1'),
('Fujifilm', 'Japonia', 'Tokio, Akasaka 9-7-3'),
('Olympus', 'Japonia', 'Tokio, Shinjuku 2-3-1'),
('Panasonic', 'Japonia', 'Osaka, Kadoma 1006'),
('Pentax', 'Japonia', 'Tokio, Itabashi 2-35-7'),
('Leica', 'Niemcy', 'Wetzlar, Am Leitz-Park 5'),
('Hasselblad', 'Szwecja', 'Goteborg, Ekonomivagen 2'),
('Sigma', 'Japonia', 'Kawasaki, Kurigi 2-8-15'),
('Kodak', 'USA', 'Rochester, State Street 343'),
('Ricoh', 'Japonia', 'Tokio, Ginza 8-13-1');
INSERT INTO Producent (nazwa) VALUES
('Zenit'),
('Praktica'),
('Yashica');

-- ID od 100 do 114.
INSERT INTO Matryca (przekatna, rozdzielczosc, typ) VALUES
(43.27, 24.2, 'CMOS'),
(43.27, 45.7, 'BSI-CMOS'),
(43.27, 61.0, 'BSI-CMOS'),
(28.40, 24.1, 'CMOS'),
(28.40, 26.1, 'X-Trans'),
(26.80, 32.5, 'CMOS'),
(21.64, 20.4, 'Live MOS'),
(21.64, 25.2, 'Live MOS'),
(15.86, 20.1, 'BSI-CMOS'),
(7.66, 16.0, 'CCD'),
(7.66, 12.1, 'CMOS'),
(55.00, 50.0, 'CMOS'),
(54.78, 51.4, 'BSI-CMOS'),
(31.10, 14.8, 'Foveon'),
(9.50, 10.0, 'CCD');

-- minPrzeslona z różnych przedziałów (przyda się w zad. 6).
INSERT INTO Obiektyw (model, minPrzeslona, maxPrzeslona) VALUES
('EF 50mm f/1.2L', 1.2, 16),
('EF 50mm f/1.8 STM', 1.8, 22),
('RF 24-105mm f/4L', 4.0, 22),
('AF-S 85mm f/1.4G', 1.4, 16),
('Z 24-70mm f/2.8 S', 2.8, 22),
('FE 70-200mm f/4 G', 4.0, 22),
('FE 35mm f/1.8', 1.8, 22),
('XF 23mm f/2 R WR', 2.0, 16),
('M.Zuiko 12-40mm f/2.8', 2.8, 22),
('Lumix 25mm f/1.7', 1.7, 16),
('HD DA 55-300mm f/4.5', 4.5, 32),
('Summilux-M 50mm f/1.4', 1.4, 16),
('XCD 45mm f/3.5', 3.5, 32),
('Art 18-35mm f/1.8', 1.8, 16),
('GR 18.3mm f/2.8', 2.8, 16);

INSERT INTO Aparat (model, producent, matryca, obiektyw, waga, typ) VALUES
('EOS R6', 1, 100, 3, 680, 'profesjonalny'),
('EOS 250D', 1, 103, 2, 449, 'lustrzanka'),
('EOS R5', 1, 101, 1, 738, 'profesjonalny'),
('D850', 2, 101, 4, 1005, 'lustrzanka'),
('Z6 II', 2, 100, 5, 705, 'profesjonalny'),
('A7R IV', 3, 102, 6, 665, 'profesjonalny'),
('RX100 VII', 3, 108, 7, 302, 'kompaktowy'),
('X-T4', 4, 104, 8, 607, 'inny'),
('OM-D E-M1 III', 5, 107, 9, 580, 'inny'),
('Lumix G9', 6, 106, 10, 658, 'inny'),
('K-1 II', 7, 100, 11, 1010, 'lustrzanka'),
('M11', 8, 102, 12, 640, 'profesjonalny'),
('X2D 100C', 9, 112, 13, 895, 'profesjonalny'),
('sd Quattro', 10, 113, 14, 625, 'inny'),
('GR III', 12, 103, 15, 257, 'kompaktowy');

SELECT COUNT(*) AS producenci FROM Producent;
SELECT COUNT(*) AS matryce FROM Matryca;
SELECT COUNT(*) AS obiektywy FROM Obiektyw;
SELECT COUNT(*) AS aparaty FROM Aparat;
