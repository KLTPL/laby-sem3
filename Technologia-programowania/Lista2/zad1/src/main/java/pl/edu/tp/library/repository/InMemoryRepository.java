package pl.edu.tp.library.repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.ToLongFunction;

/**
 * Repozytorium w pamięci; dane znikają po zamknięciu programu (zgodnie z treścią zadania).
 *
 * <p>GRASP Pure Fabrication: jedna generyczna klasa obsługuje wszystkie typy, więc
 * klasy dziedzinowe nie wiedzą nic o przechowywaniu.
 *
 * @param <T> typ przechowywanych obiektów
 */
public final class InMemoryRepository<T> implements Repository<T> {

  private final Map<Long, T> items = new LinkedHashMap<>();
  private final ToLongFunction<T> idOf;
  private long lastId;

  /**
   * Tworzy puste repozytorium.
   *
   * @param idOf funkcja odczytująca identyfikator obiektu
   */
  public InMemoryRepository(ToLongFunction<T> idOf) {
    this.idOf = Objects.requireNonNull(idOf, "idOf");
  }

  @Override
  public long nextId() {
    lastId++;
    return lastId;
  }

  @Override
  public void save(T item) {
    Objects.requireNonNull(item, "item");
    items.put(idOf.applyAsLong(item), item);
  }

  @Override
  public Optional<T> findById(long id) {
    return Optional.ofNullable(items.get(id));
  }

  @Override
  public List<T> findAll() {
    return List.copyOf(items.values());
  }
}
