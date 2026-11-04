package pl.edu.tp.library.domain;

import java.util.Objects;

/**
 * Tytuł książki. Egzemplarze są osobnymi obiektami {@link Copy}.
 *
 * <p>GRASP Information Expert: książka zna tylko własne dane (tytuł, autor). Nie wie
 * o egzemplarzach, dzięki czemu ma niskie sprzężenie.
 *
 * @param id identyfikator nadany przez repozytorium
 * @param title tytuł
 * @param author autor
 */
public record Book(long id, String title, String author) {

  /** Odrzuca brakujące (null) pola. */
  public Book {
    Objects.requireNonNull(title, "title");
    Objects.requireNonNull(author, "author");
  }
}
