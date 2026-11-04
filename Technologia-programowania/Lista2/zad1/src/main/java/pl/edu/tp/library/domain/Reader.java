package pl.edu.tp.library.domain;

import java.util.Objects;

/**
 * Czytelnik biblioteki.
 *
 * <p>GRASP Low Coupling: czytelnik nie przechowuje wypożyczeń. Wypożyczenia są
 * w {@link Copy}, więc informacja nie jest zapisana w dwóch miejscach.
 *
 * @param id identyfikator nadany przez repozytorium
 * @param name imię i nazwisko
 */
public record Reader(long id, String name) {

  /** Odrzuca brakujące (null) imię. */
  public Reader {
    Objects.requireNonNull(name, "name");
  }
}
