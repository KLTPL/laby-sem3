package pl.edu.tp.library.domain;

import java.util.Objects;

/**
 * Tytuł książki. Egzemplarze są osobnymi obiektami {@link Copy}.
 *
 * <p>GRASP Information Expert: książka zna tylko własne dane (tytuł, autor). Nie wie
 * o egzemplarzach, dzięki czemu ma niskie sprzężenie.
 */
public final class Book {

  private final long id;
  private final String title;
  private final String author;

  /**
   * Tworzy tytuł.
   *
   * @param id identyfikator nadany przez repozytorium
   * @param title tytuł
   * @param author autor
   */
  public Book(long id, String title, String author) {
    this.id = id;
    this.title = Objects.requireNonNull(title, "title");
    this.author = Objects.requireNonNull(author, "author");
  }

  /**
   * Zwraca identyfikator tytułu.
   *
   * @return identyfikator
   */
  public long id() {
    return id;
  }

  /**
   * Zwraca tytuł.
   *
   * @return tytuł
   */
  public String title() {
    return title;
  }

  /**
   * Zwraca autora.
   *
   * @return autor
   */
  public String author() {
    return author;
  }
}
