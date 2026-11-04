package pl.edu.tp.library.ui;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import pl.edu.tp.library.domain.Book;
import pl.edu.tp.library.domain.Copy;
import pl.edu.tp.library.domain.Reader;
import pl.edu.tp.library.service.LibraryService;

/**
 * Menu konsolowe. Zbiera dane od bibliotekarza i przekazuje je do {@link LibraryService}.
 *
 * <p>GRASP High Cohesion: klasa zajmuje się tylko interfejsem użytkownika. GRASP
 * Controller: polecenia trafiają do serwisu, a nie bezpośrednio do klas dziedzinowych,
 * więc logikę można testować bez konsoli.
 */
public final class ConsoleUi {

  private static final String MENU = """

      === Biblioteka ===
      1. Dodaj czytelnika
      2. Dodaj egzemplarz
      3. Wypożycz
      4. Zwróć
      5. Pokaż książki
      6. Pokaż czytelników
      0. Wyjście""";

  private final LibraryService service;
  private final ConsoleIo io;

  /**
   * Tworzy menu.
   *
   * @param service przypadki użycia
   * @param io obsługa konsoli
   */
  public ConsoleUi(LibraryService service, ConsoleIo io) {
    this.service = Objects.requireNonNull(service, "service");
    this.io = Objects.requireNonNull(io, "io");
  }

  /** Pokazuje menu do wyboru opcji 0 albo końca wejścia. */
  public void run() {
    boolean running = true;
    while (running) {
      io.println(MENU);
      running = io.tryAsk("Wybór").map(this::handle).orElse(false);
    }
  }

  private boolean handle(String choice) {
    // Błędy danych i reguł z serwisu pokazujemy użytkownikowi; może spróbować ponownie.
    try {
      switch (choice) {
        case "1" -> addReader();
        case "2" -> addCopy();
        case "3" -> lend();
        case "4" -> giveBack();
        case "5" -> showBooks();
        case "6" -> showReaders();
        case "0" -> {
          return false;
        }
        default -> io.println("Nieznana opcja: " + choice);
      }
    } catch (IllegalArgumentException | IllegalStateException e) {
      io.println("Błąd: " + e.getMessage());
    }
    return true;
  }

  private void addReader() {
    Reader reader = service.addReader(io.ask("Imię i nazwisko"));
    io.println("Dodano czytelnika [" + reader.id() + "] " + reader.name());
  }

  private void addCopy() {
    Copy copy = service.addCopy(io.ask("Tytuł"), io.ask("Autor"));
    io.println("Dodano egzemplarz #" + copy.id() + " książki " + describe(copy.book()));
  }

  private void lend() {
    Optional<Reader> reader = chooseReader();
    if (reader.isEmpty()) {
      return;
    }
    Optional<Book> book = io.choose(service.findBooksByTitle(io.ask("Tytuł")),
        ConsoleUi::describe, Book::id);
    if (book.isEmpty()) {
      return;
    }
    Optional<Copy> free = service.copiesOf(book.get().id()).stream()
        .filter(Copy::isAvailable)
        .findFirst();
    if (free.isEmpty()) {
      io.println("Wszystkie egzemplarze tej książki są wypożyczone.");
      return;
    }
    service.lend(free.get().id(), reader.get().id());
    io.println("Wypożyczono egzemplarz #" + free.get().id() + " czytelnikowi "
        + describe(reader.get()));
  }

  private void giveBack() {
    Optional<Reader> reader = chooseReader();
    if (reader.isEmpty()) {
      return;
    }
    Optional<Copy> copy = io.choose(service.copiesHeldBy(reader.get().id()),
        c -> "egzemplarz " + describe(c.book()), Copy::id);
    if (copy.isEmpty()) {
      return;
    }
    service.giveBack(copy.get().id());
    io.println("Zwrócono egzemplarz #" + copy.get().id());
  }

  private Optional<Reader> chooseReader() {
    return io.choose(service.findReadersByName(io.ask("Czytelnik")),
        ConsoleUi::describe, Reader::id);
  }

  private void showBooks() {
    List<Book> books = service.books();
    if (books.isEmpty()) {
      io.println("Brak książek.");
    }
    for (Book book : books) {
      io.println("[" + book.id() + "] " + describe(book));
      for (Copy copy : service.copiesOf(book.id())) {
        io.println("    egzemplarz #" + copy.id() + ": " + status(copy));
      }
    }
  }

  private void showReaders() {
    List<Reader> readers = service.readers();
    if (readers.isEmpty()) {
      io.println("Brak czytelników.");
    }
    for (Reader reader : readers) {
      io.println("[" + reader.id() + "] " + reader.name());
      for (Copy copy : service.copiesHeldBy(reader.id())) {
        io.println("    ma egzemplarz #" + copy.id() + ": " + describe(copy.book()));
      }
    }
  }

  private static String status(Copy copy) {
    return copy.currentReader()
        .map(r -> "wypożyczony przez " + r.name() + " od "
            + copy.loans().getLast().loanDate())
        .orElse("dostępny") + ", wypożyczeń w historii: " + copy.loans().size();
  }

  private static String describe(Book book) {
    return "„" + book.title() + "” – " + book.author();
  }

  private static String describe(Reader reader) {
    return reader.name();
  }
}
