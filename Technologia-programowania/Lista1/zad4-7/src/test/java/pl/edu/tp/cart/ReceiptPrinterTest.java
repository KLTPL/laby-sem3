package pl.edu.tp.cart;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ReceiptPrinterTest {

  @Test
  void receiptContainsItemsAndTotals() {
    Cart cart = new Cart();
    cart.add(new Product("Pen", 250), 2);

    String receipt = new ReceiptPrinter().render(cart, new PercentageDiscount(10));

    assertTrue(receipt.contains("Pen"));
    assertTrue(receipt.contains("Subtotal: 5.00"));
    assertTrue(receipt.contains("Discount: -0.50"));
    assertTrue(receipt.contains("Total: 4.50"));
  }
}
