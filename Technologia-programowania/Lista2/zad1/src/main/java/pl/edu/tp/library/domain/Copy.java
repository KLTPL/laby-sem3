package pl.edu.tp.library.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Fizyczny egzemplarz książki z historią wypożyczeń (najnowsze na końcu).
 *
 * <p>GRASP Information Expert: egzemplarz ma całą swoją historię, więc to on sprawdza
 * regułę „nie wypożyczaj nieoddanego egzemplarza”. GRASP Creator: egzemplarz
 * przechowuje wypożyczenia, więc to on je tworzy.
 */
public final class Copy {

  private final long id;
  private final Book book;
  private final List<Loan> loans = new ArrayList<>();

  /**
   * Tworzy egzemplarz bez historii wypożyczeń.
   *
   * @param id identyfikator nadany przez repozytorium
   * @param book tytuł, którego to egzemplarz
   */
  public Copy(long id, Book book) {
    this.id = id;
    this.book = Objects.requireNonNull(book, "book");
  }

  /**
   * Zwraca identyfikator egzemplarza.
   *
   * @return identyfikator
   */
  public long id() {
    return id;
  }

  /**
   * Zwraca tytuł, którego to egzemplarz.
   *
   * @return książka
   */
  public Book book() {
    return book;
  }

  /**
   * Zwraca historię wypożyczeń tylko do odczytu.
   *
   * @return wypożyczenia od najstarszego
   */
  public List<Loan> loans() {
    return Collections.unmodifiableList(loans);
  }

  /**
   * Sprawdza, czy egzemplarz można wypożyczyć.
   *
   * @return {@code true}, jeśli nie ma otwartego wypożyczenia
   */
  public boolean isAvailable() {
    return loans.isEmpty() || !loans.getLast().isOpen();
  }

  /**
   * Zwraca czytelnika, który ma teraz egzemplarz.
   *
   * @return czytelnik albo pusta wartość, jeśli egzemplarz jest w bibliotece
   */
  public Optional<Reader> currentReader() {
    if (isAvailable()) {
      return Optional.empty();
    }
    return Optional.of(loans.getLast().reader());
  }

  /**
   * Wypożycza egzemplarz czytelnikowi.
   *
   * @param reader czytelnik
   * @param date data wypożyczenia
   * @return nowe wypożyczenie
   * @throws IllegalStateException gdy egzemplarz jest już wypożyczony
   */
  public Loan lend(Reader reader, LocalDate date) {
    if (!isAvailable()) {
      throw new IllegalStateException("Egzemplarz #" + id + " jest już wypożyczony");
    }
    Loan loan = new Loan(reader, date);
    loans.add(loan);
    return loan;
  }

  /**
   * Przyjmuje zwrot egzemplarza.
   *
   * @param date data oddania
   * @throws IllegalStateException gdy egzemplarz nie jest wypożyczony
   */
  public void giveBack(LocalDate date) {
    if (isAvailable()) {
      throw new IllegalStateException("Egzemplarz #" + id + " nie jest wypożyczony");
    }
    loans.getLast().markReturned(date);
  }
}
