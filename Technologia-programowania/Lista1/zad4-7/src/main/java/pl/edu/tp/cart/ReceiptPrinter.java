package pl.edu.tp.cart;

import java.util.Locale;
import java.util.Map;

/**
 * Renders a {@link Cart} as a plain text receipt.
 */
public class ReceiptPrinter {

  private static final int CENTS_IN_UNIT = 100;

  /**
   * Builds the receipt text.
   *
   * @param cart cart to print
   * @param policy discount applied to the cart
   * @return multi-line receipt
   */
  public String render(Cart cart, DiscountPolicy policy) {
    StringBuilder receipt = new StringBuilder();
    for (Map.Entry<Product, Integer> entry : cart.items().entrySet()) {
      Product product = entry.getKey();
      int quantity = entry.getValue();
      receipt.append(String.format(Locale.ROOT, "%-12s %2d x %8s%n",
          product.name(), quantity, formatMoney(product.priceInCents())));
    }
    long subtotal = cart.subtotalInCents();
    receipt.append(String.format(Locale.ROOT, "Subtotal: %s%n", formatMoney(subtotal)));
    receipt.append(String.format(Locale.ROOT, "Discount: -%s%n",
        formatMoney(policy.discountFor(subtotal))));
    receipt.append(String.format(Locale.ROOT, "Total: %s%n",
        formatMoney(cart.totalInCents(policy))));
    return receipt.toString();
  }

  private static String formatMoney(long cents) {
    return String.format(Locale.ROOT, "%d.%02d", cents / CENTS_IN_UNIT, cents % CENTS_IN_UNIT);
  }
}
