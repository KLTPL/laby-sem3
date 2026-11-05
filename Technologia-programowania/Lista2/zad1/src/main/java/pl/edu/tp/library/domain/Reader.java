package pl.edu.tp.library.domain;

import java.util.Objects;

/**
 * Czytelnik biblioteki.
 *
 * <p>GRASP Low Coupling: czytelnik nie przechowuje wypożyczeń. Wypożyczenia są
 * w {@link Copy}, więc informacja nie jest zapisana w dwóch miejscach.
 */
public final class Reader {

  private final long id;
  private final String name;

  /**
   * Tworzy czytelnika.
   *
   * @param id identyfikator nadany przez repozytorium
   * @param name imię i nazwisko
   */
  public Reader(long id, String name) {
    this.id = id;
    this.name = Objects.requireNonNull(name, "name");
  }

  /**
   * Zwraca identyfikator czytelnika.
   *
   * @return identyfikator
   */
  public long id() {
    return id;
  }

  /**
   * Zwraca imię i nazwisko.
   *
   * @return imię i nazwisko
   */
  public String name() {
    return name;
  }
}
