package pl.edu.tp.cart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CartTest {

  private static final Product PEN = new Product("Pen", 250);
  private static final Product BOOK = new Product("Book", 1000);

  @Test
  void subtotalSumsPriceTimesQuantity() {
    Cart cart = new Cart();
    cart.add(PEN, 2);
    cart.add(BOOK, 1);

    assertEquals(1500, cart.subtotalInCents());
  }

  @Test
  void addingSameProductTwiceIncreasesQuantity() {
    Cart cart = new Cart();
    cart.add(PEN, 1);
    cart.add(PEN, 2);

    assertEquals(3, cart.items().get(PEN));
  }

  @Test
  void totalAppliesDiscount() {
    Cart cart = new Cart();
    cart.add(BOOK, 2);

    assertEquals(1800, cart.totalInCents(new PercentageDiscount(10)));
  }

  @Test
  void nonPositiveQuantityIsRejected() {
    Cart cart = new Cart();

    assertThrows(IllegalArgumentException.class, () -> cart.add(PEN, 0));
  }
}
