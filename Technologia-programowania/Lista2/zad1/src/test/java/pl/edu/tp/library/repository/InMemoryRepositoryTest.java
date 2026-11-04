package pl.edu.tp.library.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import pl.edu.tp.library.domain.Reader;

class InMemoryRepositoryTest {

  private final Repository<Reader> repository = new InMemoryRepository<>(Reader::id);

  @Test
  void idsIncrease() {
    assertEquals(1, repository.nextId());
    assertEquals(2, repository.nextId());
  }

  @Test
  void unknownIdIsEmpty() {
    assertEquals(Optional.empty(), repository.findById(42));
  }

  @Test
  void savedItemCanBeFound() {
    Reader anna = new Reader(repository.nextId(), "Anna");

    repository.save(anna);

    assertEquals(Optional.of(anna), repository.findById(anna.id()));
  }

  @Test
  void findAllKeepsInsertionOrder() {
    Reader anna = new Reader(repository.nextId(), "Anna");
    Reader jan = new Reader(repository.nextId(), "Jan");
    repository.save(anna);
    repository.save(jan);

    assertEquals(List.of(anna, jan), repository.findAll());
  }

  @Test
  void savingSameIdReplacesItem() {
    repository.save(new Reader(1, "Anna"));
    Reader renamed = new Reader(1, "Anna Nowak");

    repository.save(renamed);

    assertEquals(List.of(renamed), repository.findAll());
  }
}
