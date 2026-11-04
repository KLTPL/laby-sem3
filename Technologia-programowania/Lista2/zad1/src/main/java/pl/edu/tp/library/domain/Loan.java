package pl.edu.tp.library.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

/**
 * Jedno wypożyczenie egzemplarza przez czytelnika.
 *
 * <p>GRASP Information Expert: wypożyczenie zna swoje daty, więc samo odpowiada na
 * pytanie, czy jest jeszcze otwarte. Konstruktor jest pakietowy: tworzy je tylko
 * {@link Copy} (GRASP Creator).
 */
public final class Loan {

  private final Reader reader;
  private final LocalDate loanDate;
  private LocalDate returnDate;

  Loan(Reader reader, LocalDate loanDate) {
    this.reader = Objects.requireNonNull(reader, "reader");
    this.loanDate = Objects.requireNonNull(loanDate, "loanDate");
  }

  /**
   * Zwraca czytelnika, który wypożyczył egzemplarz.
   *
   * @return czytelnik
   */
  public Reader reader() {
    return reader;
  }

  /**
   * Zwraca datę wypożyczenia.
   *
   * @return data wypożyczenia
   */
  public LocalDate loanDate() {
    return loanDate;
  }

  /**
   * Zwraca datę oddania; pusta, dopóki egzemplarz nie wrócił.
   *
   * @return data oddania
   */
  public Optional<LocalDate> returnDate() {
    return Optional.ofNullable(returnDate);
  }

  /**
   * Sprawdza, czy egzemplarz jest wciąż u czytelnika.
   *
   * @return {@code true}, jeśli brak daty oddania
   */
  public boolean isOpen() {
    return returnDate == null;
  }

  void markReturned(LocalDate date) {
    Objects.requireNonNull(date, "date");
    if (date.isBefore(loanDate)) {
      throw new IllegalArgumentException("Data oddania jest wcześniejsza niż data wypożyczenia");
    }
    returnDate = date;
  }
}
