package pl.edu.tp.library.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import pl.edu.tp.library.domain.Book;
import pl.edu.tp.library.domain.Copy;
import pl.edu.tp.library.domain.Reader;
import pl.edu.tp.library.repository.Repository;

/**
 * Przypadki użycia biblioteki: dodawanie, wypożyczanie, zwrot i wyszukiwanie.
 *
 * <p>GRASP Controller: przyjmuje polecenia z interfejsu i koordynuje operację, ale samą
 * regułę wypożyczenia deleguje do {@link Copy} (Information Expert). GRASP Low Coupling:
 * zależy tylko od interfejsu {@link Repository} i od {@link Clock}, więc nie zna ani
 * konsoli, ani sposobu zapisu. GRASP High Cohesion: nie wypisuje niczego na ekran.
 */
public class LibraryService {

  private final Repository<Book> books;
  private final Repository<Copy> copies;
  private final Repository<Reader> readers;
  private final Clock clock;

  /**
   * Tworzy serwis na podanych repozytoriach.
   *
   * @param books repozytorium tytułów
   * @param copies repozytorium egzemplarzy
   * @param readers repozytorium czytelników
   * @param clock zegar dający datę wypożyczenia i zwrotu
   */
  public LibraryService(Repository<Book> books, Repository<Copy> copies,
      Repository<Reader> readers, Clock clock) {
    this.books = Objects.requireNonNull(books, "books");
    this.copies = Objects.requireNonNull(copies, "copies");
    this.readers = Objects.requireNonNull(readers, "readers");
    this.clock = Objects.requireNonNull(clock, "clock");
  }

  /**
   * Dodaje czytelnika.
   *
   * @param name imię i nazwisko
   * @return nowy czytelnik
   */
  public Reader addReader(String name) {
    Reader reader = new Reader(readers.nextId(), requireText(name, "Imię"));
    readers.save(reader);
    return reader;
  }

  /**
   * Dodaje egzemplarz tytułu. Jeśli tytułu z tym autorem jeszcze nie ma, najpierw go
   * dodaje.
   *
   * @param title tytuł
   * @param author autor
   * @return nowy egzemplarz
   */
  public Copy addCopy(String title, String author) {
    String cleanTitle = requireText(title, "Tytuł");
    String cleanAuthor = requireText(author, "Autor");
    Book book = books.findAll().stream()
        .filter(b -> b.title().equalsIgnoreCase(cleanTitle)
            && b.author().equalsIgnoreCase(cleanAuthor))
        .findFirst()
        .orElseGet(() -> addBook(cleanTitle, cleanAuthor));
    Copy copy = new Copy(copies.nextId(), book);
    copies.save(copy);
    return copy;
  }

  /**
   * Wypożycza egzemplarz czytelnikowi z dzisiejszą datą.
   *
   * @param copyId identyfikator egzemplarza
   * @param readerId identyfikator czytelnika
   * @return wypożyczony egzemplarz
   * @throws IllegalArgumentException gdy nie ma takiego egzemplarza lub czytelnika
   * @throws IllegalStateException gdy egzemplarz jest już wypożyczony
   */
  public Copy lend(long copyId, long readerId) {
    Copy copy = require(copies.findById(copyId), "egzemplarza", copyId);
    Reader reader = require(readers.findById(readerId), "czytelnika", readerId);
    copy.lend(reader, LocalDate.now(clock));
    copies.save(copy);
    return copy;
  }

  /**
   * Przyjmuje zwrot egzemplarza z dzisiejszą datą.
   *
   * @param copyId identyfikator egzemplarza
   * @return oddany egzemplarz
   * @throws IllegalArgumentException gdy nie ma takiego egzemplarza
   * @throws IllegalStateException gdy egzemplarz nie jest wypożyczony
   */
  public Copy giveBack(long copyId) {
    Copy copy = require(copies.findById(copyId), "egzemplarza", copyId);
    copy.giveBack(LocalDate.now(clock));
    copies.save(copy);
    return copy;
  }

  /**
   * Zwraca wszystkie tytuły.
   *
   * @return tytuły w kolejności dodania
   */
  public List<Book> books() {
    return books.findAll();
  }

  /**
   * Zwraca wszystkich czytelników.
   *
   * @return czytelnicy w kolejności dodania
   */
  public List<Reader> readers() {
    return readers.findAll();
  }

  /**
   * Szuka tytułów zawierających podany tekst, bez rozróżniania wielkości liter.
   *
   * @param fragment fragment tytułu
   * @return pasujące tytuły
   */
  public List<Book> findBooksByTitle(String fragment) {
    return matching(books.findAll(), Book::title, fragment);
  }

  /**
   * Szuka czytelników, których imię zawiera podany tekst, bez rozróżniania wielkości liter.
   *
   * @param fragment fragment imienia
   * @return pasujący czytelnicy
   */
  public List<Reader> findReadersByName(String fragment) {
    return matching(readers.findAll(), Reader::name, fragment);
  }

  /**
   * Zwraca egzemplarze danego tytułu.
   *
   * @param bookId identyfikator tytułu
   * @return egzemplarze tytułu
   */
  public List<Copy> copiesOf(long bookId) {
    return copies.findAll().stream()
        .filter(c -> c.book().id() == bookId)
        .toList();
  }

  /**
   * Zwraca egzemplarze, które czytelnik ma teraz u siebie.
   *
   * @param readerId identyfikator czytelnika
   * @return wypożyczone i nieoddane egzemplarze
   */
  public List<Copy> copiesHeldBy(long readerId) {
    return copies.findAll().stream()
        .filter(c -> c.currentReader().map(r -> r.id() == readerId).orElse(false))
        .toList();
  }

  private Book addBook(String title, String author) {
    Book book = new Book(books.nextId(), title, author);
    books.save(book);
    return book;
  }

  private static <T> List<T> matching(List<T> items, Function<T, String> text, String fragment) {
    String needle = fragment == null ? "" : fragment.trim().toLowerCase(Locale.ROOT);
    return items.stream()
        .filter(item -> text.apply(item).toLowerCase(Locale.ROOT).contains(needle))
        .toList();
  }

  private static <T> T require(Optional<T> found, String what, long id) {
    return found.orElseThrow(
        () -> new IllegalArgumentException("Nie ma " + what + " o numerze " + id));
  }

  private static String requireText(String value, String field) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(field + " nie może być puste");
    }
    return value.trim();
  }
}
