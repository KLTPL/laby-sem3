# Zadanie 4

Polecenia uruchamiamy z katalogu `Bazy-danych`.

Utworzenie i wywołanie procedury jako `fotograf`:

```bash
docker compose exec -T mysql mysql -ufotograf -p'Fotograf123!' -t aparaty < Lista2/zadanie4.sql
```

Kolejne wywołanie (doda następne 100 modeli):

```bash
docker compose exec -T mysql mysql -ufotograf -p'Fotograf123!' -t aparaty -e "CALL generuj_aparaty();"
```

Odpowiedzi na pytania są na końcu `zadanie4.sql`. `SHOW GRANTS` pokazuje,
że `fotograf` dostał `EXECUTE, ALTER ROUTINE` na `generuj_aparaty`.

Ponowne utworzenie procedury (twórca ma już `ALTER ROUTINE`, więc może ją usunąć):

```bash
docker compose exec -T mysql mysql -ufotograf -p'Fotograf123!' aparaty -e "DROP PROCEDURE generuj_aparaty;"
```
