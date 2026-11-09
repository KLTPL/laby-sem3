# Zadanie 3

Polecenia uruchamiamy z katalogu `Bazy-danych`.

Połączenie jako `fotograf` (konto z zad. 1):

```bash
docker compose exec -it mysql mysql -ufotograf -p'Fotograf123!' aparaty
```

Wstawienie po 15 rekordów do każdej tabeli z pliku:

```bash
docker compose exec -T mysql mysql -ufotograf -p'Fotograf123!' -t aparaty < Lista2/zadanie3.sql
```

Sprawdzenie, kim jesteśmy (w kliencie):

```sql
SELECT CURRENT_USER();
```
