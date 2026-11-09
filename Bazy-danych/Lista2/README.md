# Lista 2

Polecenia uruchamiamy z katalogu `Bazy-danych` (MySQL 8.4 z `docker-compose.yaml`).
Komenda do każdego pliku `.sql` jest w komentarzu na jego początku.

| Zad. | Pliki | Uruchamia |
| --- | --- | --- |
| 1 | `zadanie1.sql` | root (użytkownik `fotograf` / `Fotograf123!`; odbiera i ponownie nadaje UPDATE) |
| 2 | `zadanie2.sql` | root |
| 3 | `zadanie3.md` + `zadanie3.sql` | fotograf |
| 4 | `zadanie4.md` + `zadanie4.sql` | fotograf, odpowiedzi na pytania w komentarzach |
| 5–7 | `zadanie5.sql`, `zadanie6.sql`, `zadanie7.sql` | root, testy w plikach (zad. 7 w transakcji z ROLLBACK) |
| 8 | `zadanie8.md` + `zadanie8.sql` | root, test uprawnień na użytkowniku `wstawiacz` |

## Uwagi do rozwiązań

### Zadanie 2

MySQL nie pozwala na CHECK dla kolumny AUTO_INCREMENT (błąd 3818), więc ID nie ma
sprawdzenia nieujemności.

### Zadanie 4

- Modele się nie powtarzają: `model` jest kluczem głównym, a procedura przy duplikacie
  losuje nazwę jeszcze raz.
- `fotograf` może utworzyć i wywołać procedurę: ma CREATE ROUTINE, a EXECUTE
  i ALTER ROUTINE dostaje automatycznie jako twórca.
- W pliku nie ma `DROP PROCEDURE IF EXISTS`: przy pierwszym uruchomieniu `fotograf`
  nie ma jeszcze uprawnień do tej procedury i dostaje błąd 1370.

### Zadanie 8

- MySQL 8.4 gubi ostrzeżenie z `SIGNAL SQLSTATE '01000'` w wyzwalaczu
  (`SHOW WARNINGS` jest puste; w procedurze ten sam `SIGNAL` działa).
  `SIGNAL` zostaje, a komunikat trafia dodatkowo do zmiennej `@komunikat`.
- Uprawnienia zależą od twórcy wyzwalacza (`DEFINER`):
  - twórca root: użytkownikowi wystarczy INSERT na `Aparat`;
  - twórca to sam użytkownik: INSERT, SELECT i TRIGGER na `Aparat`
    oraz SELECT i INSERT na `Producent`.

  Kolejność błędów przy dokładaniu uprawnień jest w tabeli w `zadanie8.md`.
