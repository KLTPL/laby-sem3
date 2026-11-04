package pl.edu.tp.library;

import java.time.Clock;
import pl.edu.tp.library.domain.Book;
import pl.edu.tp.library.domain.Copy;
import pl.edu.tp.library.domain.Reader;
import pl.edu.tp.library.repository.InMemoryRepository;
import pl.edu.tp.library.repository.Repository;
import pl.edu.tp.library.service.LibraryService;
import pl.edu.tp.library.ui.ConsoleIo;
import pl.edu.tp.library.ui.ConsoleUi;

/**
 * Punkt startowy: składa aplikację z części.
 *
 * <p>Dependency Inversion: to jedyne miejsce, które zna konkretne repozytoria. Aby
 * zapisywać dane w bazie, wystarczy tu podmienić {@link InMemoryRepository} na
 * implementację {@link Repository} dla bazy (np. JDBC); serwis i dziedzina się nie
 * zmieniają.
 */
public final class App {

  private App() {
  }

  /**
   * Uruchamia menu konsolowe.
   *
   * @param args nieużywane
   */
  public static void main(String[] args) {
    Repository<Book> books = new InMemoryRepository<>(Book::id);
    Repository<Copy> copies = new InMemoryRepository<>(Copy::id);
    Repository<Reader> readers = new InMemoryRepository<>(Reader::id);
    LibraryService service =
        new LibraryService(books, copies, readers, Clock.systemDefaultZone());
    new ConsoleUi(service, new ConsoleIo(System.in, System.out)).run();
  }
}
