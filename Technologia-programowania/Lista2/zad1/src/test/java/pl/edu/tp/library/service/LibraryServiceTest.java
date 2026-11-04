package pl.edu.tp.library.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import pl.edu.tp.library.domain.Book;
import pl.edu.tp.library.domain.Copy;
import pl.edu.tp.library.domain.Reader;
import pl.edu.tp.library.repository.InMemoryRepository;

class LibraryServiceTest {

  private static final Instant NOW = Instant.parse("2026-11-04T10:00:00Z");

  private final LibraryService service = new LibraryService(
      new InMemoryRepository<>(Book::id),
      new InMemoryRepository<>(Copy::id),
      new InMemoryRepository<>(Reader::id),
      Clock.fixed(NOW, ZoneOffset.UTC));

  @Test
  void addingCopyOfNewTitleAddsTheTitle() {
    Copy copy = service.addCopy("Lalka", "Bolesław Prus");

    assertEquals(List.of(copy.book()), service.books());
  }

  @Test
  void addingCopyOfExistingTitleReusesIt() {
    Copy first = service.addCopy("Lalka", "Bolesław Prus");
    Copy second = service.addCopy("lalka ", "bolesław prus");

    assertEquals(first.book(), second.book());
    assertNotEquals(first.id(), second.id());
    assertEquals(1, service.books().size());
    assertEquals(List.of(first, second), service.copiesOf(first.book().id()));
  }

  @Test
  void sameTitleByOtherAuthorIsSeparateBook() {
    service.addCopy("Wiersze", "Adam Mickiewicz");
    service.addCopy("Wiersze", "Juliusz Słowacki");

    assertEquals(2, service.books().size());
  }

  @Test
  void blankNameIsRejected() {
    assertThrows(IllegalArgumentException.class, () -> service.addReader("  "));
    assertThrows(IllegalArgumentException.class, () -> service.addCopy("", "Prus"));
  }

  @Test
  void lendingUsesTodayFromClock() {
    Reader anna = service.addReader("Anna Nowak");
    Copy copy = service.addCopy("Lalka", "Bolesław Prus");

    service.lend(copy.id(), anna.id());

    assertEquals(LocalDate.of(2026, 11, 4), copy.loans().getFirst().loanDate());
  }

  @Test
  void unknownIdsAreRejected() {
    Reader anna = service.addReader("Anna Nowak");
    Copy copy = service.addCopy("Lalka", "Bolesław Prus");

    assertThrows(IllegalArgumentException.class, () -> service.lend(99, anna.id()));
    assertThrows(IllegalArgumentException.class, () -> service.lend(copy.id(), 99));
    assertThrows(IllegalArgumentException.class, () -> service.giveBack(99));
  }

  @Test
  void readerCanHoldSeveralCopies() {
    Reader anna = service.addReader("Anna Nowak");
    Copy lalka = service.addCopy("Lalka", "Bolesław Prus");
    Copy potop = service.addCopy("Potop", "Henryk Sienkiewicz");

    service.lend(lalka.id(), anna.id());
    service.lend(potop.id(), anna.id());

    assertEquals(List.of(lalka, potop), service.copiesHeldBy(anna.id()));
  }

  @Test
  void returnedCopyIsNoLongerHeld() {
    Reader anna = service.addReader("Anna Nowak");
    Copy copy = service.addCopy("Lalka", "Bolesław Prus");
    service.lend(copy.id(), anna.id());

    service.giveBack(copy.id());

    assertEquals(List.of(), service.copiesHeldBy(anna.id()));
    assertEquals(Optional.empty(), copy.currentReader());
  }

  @Test
  void searchIgnoresCase() {
    Reader jan = service.addReader("Jan Kowalski");
    service.addReader("Anna Nowak");
    Copy copy = service.addCopy("Pan Tadeusz", "Adam Mickiewicz");

    assertEquals(List.of(jan), service.findReadersByName("KOWAL"));
    assertEquals(List.of(copy.book()), service.findBooksByTitle("tadeusz"));
  }
}
