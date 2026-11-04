package pl.edu.tp.library.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class CopyTest {

  private static final LocalDate DAY1 = LocalDate.of(2026, 11, 1);
  private static final LocalDate DAY2 = LocalDate.of(2026, 11, 2);

  private final Book book = new Book(1, "Lalka", "Bolesław Prus");
  private final Reader anna = new Reader(1, "Anna Nowak");
  private final Reader jan = new Reader(2, "Jan Kowalski");

  @Test
  void newCopyIsAvailable() {
    Copy copy = new Copy(1, book);

    assertTrue(copy.isAvailable());
    assertEquals(Optional.empty(), copy.currentReader());
    assertTrue(copy.loans().isEmpty());
  }

  @Test
  void lendingMakesCopyUnavailable() {
    Copy copy = new Copy(1, book);

    copy.lend(anna, DAY1);

    assertFalse(copy.isAvailable());
    assertEquals(Optional.of(anna), copy.currentReader());
  }

  @Test
  void lentCopyCannotBeLentAgain() {
    Copy copy = new Copy(1, book);
    copy.lend(anna, DAY1);

    assertThrows(IllegalStateException.class, () -> copy.lend(jan, DAY2));
    assertEquals(1, copy.loans().size());
  }

  @Test
  void returnMakesCopyAvailableAndRecordsDate() {
    Copy copy = new Copy(1, book);
    Loan loan = copy.lend(anna, DAY1);

    copy.giveBack(DAY2);

    assertTrue(copy.isAvailable());
    assertFalse(loan.isOpen());
    assertEquals(Optional.of(DAY2), loan.returnDate());
  }

  @Test
  void availableCopyCannotBeReturned() {
    Copy copy = new Copy(1, book);

    assertThrows(IllegalStateException.class, () -> copy.giveBack(DAY1));
  }

  @Test
  void returnBeforeLoanDateIsRejected() {
    Copy copy = new Copy(1, book);
    copy.lend(anna, DAY2);

    assertThrows(IllegalArgumentException.class, () -> copy.giveBack(DAY1));
    assertFalse(copy.isAvailable());
  }

  @Test
  void historyKeepsLoansInOrder() {
    Copy copy = new Copy(1, book);
    copy.lend(anna, DAY1);
    copy.giveBack(DAY1);
    copy.lend(jan, DAY2);

    assertEquals(anna, copy.loans().get(0).reader());
    assertEquals(jan, copy.loans().get(1).reader());
    assertEquals(Optional.of(jan), copy.currentReader());
  }

  @Test
  void historyCannotBeModifiedFromOutside() {
    Copy copy = new Copy(1, book);
    copy.lend(anna, DAY1);

    assertThrows(UnsupportedOperationException.class, () -> copy.loans().clear());
  }
}
