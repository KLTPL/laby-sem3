package pl.edu.tp.cart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class PercentageDiscountTest {

  @Test
  void takesPercentageOffSubtotal() {
    assertEquals(250, new PercentageDiscount(25).discountFor(1000));
  }

  @Test
  void zeroPercentGivesNoDiscount() {
    assertEquals(0, new PercentageDiscount(0).discountFor(1000));
  }

  @Test
  void percentOutOfRangeIsRejected() {
    assertThrows(IllegalArgumentException.class, () -> new PercentageDiscount(101));
    assertThrows(IllegalArgumentException.class, () -> new PercentageDiscount(-1));
  }
}
