# Diagram klas

Klasy dziedzinowe: `Book`, `Copy`, `Loan`, `Reader`. Reszta to klasy techniczne (Pure Fabrication, Controller, UI).

```mermaid
classDiagram
  class Book {
    -long id
    -String title
    -String author
    +id() long
    +title() String
    +author() String
  }
  class Copy {
    -long id
    -Book book
    -List~Loan~ loans
    +isAvailable() boolean
    +currentReader() Optional~Reader~
    +lend(Reader reader, LocalDate date) Loan
    +giveBack(LocalDate date) void
    +loans() List~Loan~
  }
  class Loan {
    -Reader reader
    -LocalDate loanDate
    -LocalDate returnDate
    +isOpen() boolean
    +returnDate() Optional~LocalDate~
    markReturned(LocalDate date) void
  }
  class Reader {
    -long id
    -String name
    +id() long
    +name() String
  }
  class Repository~T~ {
    <<interface>>
    +nextId() long
    +save(T item) void
    +findById(long id) Optional~T~
    +findAll() List~T~
  }
  class InMemoryRepository~T~ {
    -Map items
    -long lastId
  }
  class LibraryService {
    +addReader(String name) Reader
    +addCopy(String title, String author) Copy
    +lend(long copyId, long readerId) Copy
    +giveBack(long copyId) Copy
    +books() List~Book~
    +readers() List~Reader~
    +findBooksByTitle(String fragment) List~Book~
    +findReadersByName(String fragment) List~Reader~
    +copiesOf(long bookId) List~Copy~
    +copiesHeldBy(long readerId) List~Copy~
  }
  class ConsoleUi {
    +run() void
  }
  class ConsoleIo {
    +ask(String prompt) String
    +tryAsk(String prompt) Optional~String~
    +askId(String prompt) long
    +choose(List matches, Function describe, ToLongFunction idOf) Optional
    +println(String text) void
  }
  class App {
    +main(String[] args)$ void
  }

  Book "1" <-- "*" Copy : egzemplarz tytułu
  Copy "1" *-- "*" Loan : historia
  Loan "*" --> "1" Reader : wypożyczający
  Repository <|.. InMemoryRepository : implements
  LibraryService --> Repository : używa
  ConsoleUi --> LibraryService : polecenia
  ConsoleUi --> ConsoleIo : wejście/wyjście
  App ..> InMemoryRepository : tworzy
  App ..> LibraryService : tworzy
  App ..> ConsoleUi : tworzy
```

| Zapis | Znaczenie |
|---|---|
| `A <\|.. B` | B implementuje interfejs A |
| `A *-- B` | kompozycja: B nie istnieje bez A |
| `A --> B` | asocjacja: A trzyma referencję do B |
| `A ..> B` | zależność: A tylko używa B |
