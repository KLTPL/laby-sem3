# Zadanie 8

Polecenia uruchamiamy z katalogu `Bazy-danych`. Wyzwalacz jest w `zadanie8.sql`.

```bash
docker compose exec -T mysql mysql -uroot -proot -t aparaty < Lista2/zadanie8.sql
```

## Komunikat

MySQL 8.4 nie przekazuje do klienta ostrzeżenia z `SIGNAL SQLSTATE '01000'` wywołanego
w wyzwalaczu: po `INSERT` `@@warning_count` wynosi 0. Ten sam `SIGNAL` w procedurze działa.
Dlatego wyzwalacz zapisuje też komunikat w zmiennej sesyjnej `@komunikat`.

## Minimalne uprawnienia

Wyzwalacz działa z uprawnieniami swojego twórcy (`DEFINER`), a nie użytkownika,
który wykonuje `INSERT`.

| Twórca wyzwalacza | Potrzebne uprawnienia użytkownika |
| --- | --- |
| `root` (nasz przypadek) | `INSERT` na `Aparat` |
| sam użytkownik | `INSERT`, `SELECT` (odczyt `NEW.*`), `TRIGGER` na `Aparat`; `SELECT`, `INSERT` na `Producent` |

`fotograf` ma uprawnienia do całej bazy, więc do testu tworzymy osobnego użytkownika.

## Test 1: twórcą jest root

```bash
docker compose exec -T mysql mysql -uroot -proot -e "
CREATE USER 'wstawiacz'@'%' IDENTIFIED BY 'Wstawiacz123!';
GRANT INSERT ON aparaty.Aparat TO 'wstawiacz'@'%';"
```

Producent 60 nie istnieje, wyzwalacz go dodaje:

```bash
docker compose exec -T mysql mysql -uwstawiacz -p'Wstawiacz123!' -t aparaty -e "
INSERT INTO Aparat VALUES ('W1', 60, 100, 1, 200, 'inny');
SELECT @komunikat;"
docker compose exec -T mysql mysql -uroot -proot -t aparaty -e "
SELECT * FROM Producent WHERE ID = 60;"
```

Wynik: aparat wstawiony, producent 60 ma nazwę `nieznana`, `@komunikat` to
`Wstawiono nowy aparat: W1`.

## Test 2: twórcą jest użytkownik

Tworzymy wyzwalacz z `DEFINER = wstawiacz`:

```bash
sed 's/^CREATE TRIGGER/CREATE DEFINER = `wstawiacz`@`%` TRIGGER/' Lista2/zadanie8.sql \
  | docker compose exec -T mysql mysql -uroot -proot aparaty
```

Ponawiamy `INSERT` jako `wstawiacz` i dokładamy uprawnienia po kolei:

```bash
docker compose exec -T mysql mysql -uwstawiacz -p'Wstawiacz123!' -t aparaty -e "
INSERT INTO Aparat VALUES ('W2', 61, 100, 1, 200, 'inny');"
```

| Nadane uprawnienia | Wynik `INSERT` |
| --- | --- |
| `INSERT` na `Aparat` | `TRIGGER command denied ... table 'Aparat'` |
| + `TRIGGER` na `Aparat` | `SELECT command denied ... table 'Producent'` |
| + `SELECT` na `Producent` | `SELECT command denied ... column 'producent' in table 'Aparat'` |
| + `SELECT` na `Aparat` | `INSERT command denied ... table 'Producent'` dla nowego producenta |
| + `INSERT` na `Producent` | działa |

`INSERT` na `Producent` jest sprawdzany dopiero, gdy wyzwalacz naprawdę dodaje producenta,
więc wstawienie aparatu istniejącego producenta działa bez niego.

Uprawnienia nadajemy np. tak:

```bash
docker compose exec -T mysql mysql -uroot -proot -e "
GRANT TRIGGER, SELECT ON aparaty.Aparat TO 'wstawiacz'@'%';
GRANT SELECT, INSERT ON aparaty.Producent TO 'wstawiacz'@'%';
SHOW GRANTS FOR 'wstawiacz'@'%';"
```

## Sprzątanie

Usuwa dane testowe i użytkownika testowego, przywraca wyzwalacz z twórcą `root`:

```bash
docker compose exec -T mysql mysql -uroot -proot aparaty -e "
DELETE FROM Aparat WHERE model IN ('W1', 'W2');
DELETE FROM Producent WHERE ID IN (60, 61);
DROP USER 'wstawiacz'@'%';"
docker compose exec -T mysql mysql -uroot -proot aparaty < Lista2/zadanie8.sql
```
