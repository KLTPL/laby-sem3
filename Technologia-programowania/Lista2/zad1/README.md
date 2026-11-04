# Lista 2, zadanie 1: wypożyczalnia książek

Konsolowa aplikacja do wypożyczania książek. Dane są trzymane w pamięci.

## Uruchomienie

```bash
mvn clean verify
java -jar target/library-app-1.0-SNAPSHOT.jar
```

`mvn clean verify` kompiluje kod, uruchamia testy, PMD i Checkstyle (Google Java Style). Build przerywa się przy naruszeniach.

## Dokumentacja

- Diagram klas: [`docs/class-diagram.md`](docs/class-diagram.md)
- Przypadki użycia i scenariusze: [`docs/use-cases.md`](docs/use-cases.md)

## Struktura i GRASP

| Pakiet / klasa | Odpowiedzialność | Zasada |
|---|---|---|
| `domain.Book`, `domain.Reader` | dane tytułu i czytelnika | Information Expert |
| `domain.Copy` | historia wypożyczeń egzemplarza i reguła „nie wypożyczaj nieoddanego” | Information Expert, Creator (tworzy `Loan`) |
| `domain.Loan` | daty jednego wypożyczenia | Information Expert |
| `repository.Repository` | interfejs przechowywania | Pure Fabrication, Protected Variations, Dependency Inversion |
| `repository.InMemoryRepository` | przechowywanie w pamięci | Pure Fabrication |
| `service.LibraryService` | przypadki użycia | Controller, Low Coupling |
| `ui.ConsoleUi`, `ui.ConsoleIo` | menu i wejście/wyjście | High Cohesion |
| `App` | składa aplikację | Dependency Inversion |

Minimalna liczba klas dziedzinowych to cztery. `Loan` jest potrzebny, bo system pamięta daty i historię wypożyczeń. `Reader` nie przechowuje wypożyczeń, więc informacja jest tylko w jednym miejscu.

## Jak dodać bazę danych

1. Napisać klasę implementującą `Repository<T>`, np. `JdbcBookRepository implements Repository<Book>`. `nextId()` bierze numer z sekwencji, `save()` robi `INSERT` lub `UPDATE`.
2. W `App.main` podmienić `new InMemoryRepository<>(Book::id)` na nową klasę.

`LibraryService` i klasy dziedzinowe zależą tylko od interfejsu (Dependency Inversion), więc się nie zmieniają. Zmiana jest w jednym miejscu (Protected Variations).

## Testy

| Klasa testowa | Co sprawdza |
|---|---|
| `CopyTest` | wypożyczenie, zwrot, blokada podwójnego wypożyczenia, kolejność historii |
| `InMemoryRepositoryTest` | numery, wyszukiwanie, kolejność |
| `LibraryServiceTest` | dodawanie egzemplarza do istniejącego lub nowego tytułu, błędne numery, kilka książek u czytelnika, zwrot, wyszukiwanie |

## Pokaz

| Krok | Zrób |
|---|---|
| Build | `mvn clean verify` |
| Dodaj | 1 (czytelnik), 2 (egzemplarz nowego tytułu, potem drugi egzemplarz tego samego) |
| Wypożycz | 3, potem jeszcze raz na tę samą osobę, aż zabraknie egzemplarzy |
| Zwróć | 4 |
| Pokaż | 5, 6 |
