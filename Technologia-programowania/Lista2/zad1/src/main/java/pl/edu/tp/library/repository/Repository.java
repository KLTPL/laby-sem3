package pl.edu.tp.library.repository;

import java.util.List;
import java.util.Optional;

/**
 * Magazyn obiektów jednego typu.
 *
 * <p>GRASP Pure Fabrication: przechowywanie nie jest pojęciem z dziedziny, więc trafia
 * do sztucznej klasy, a klasy dziedzinowe zostają spójne. Dependency Inversion i GRASP
 * Protected Variations: serwis zależy od tego interfejsu, nie od sposobu zapisu.
 * Obsługę bazy danych dodaje się nową implementacją, np. {@code JdbcBookRepository},
 * bez zmian w serwisie i dziedzinie.
 *
 * @param <T> typ przechowywanych obiektów
 */
public interface Repository<T> {

  /**
   * Rezerwuje nowy identyfikator (w bazie danych np. z sekwencji).
   *
   * @return nieużywany identyfikator
   */
  long nextId();

  /**
   * Zapisuje nowy obiekt albo nadpisuje istniejący o tym samym identyfikatorze.
   *
   * @param item obiekt do zapisania
   */
  void save(T item);

  /**
   * Szuka obiektu po identyfikatorze.
   *
   * @param id identyfikator
   * @return znaleziony obiekt albo pusta wartość
   */
  Optional<T> findById(long id);

  /**
   * Zwraca wszystkie obiekty w kolejności dodania.
   *
   * @return lista tylko do odczytu
   */
  List<T> findAll();
}
