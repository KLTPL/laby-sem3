package pl.edu.tp.cart;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A shopping cart that keeps products together with their quantities.
 */
public class Cart {

  private final Map<Product, Integer> items = new LinkedHashMap<>();

  /**
   * Adds a product to the cart. Adding the same product again increases its quantity.
   *
   * @param product product to add
   * @param quantity how many items to add, must be positive
   */
  public void add(Product product, int quantity) {
    if (quantity <= 0) {
      throw new IllegalArgumentException("quantity must be positive");
    }
    items.merge(product, quantity, Integer::sum);
  }

  /**
   * Returns a read-only view of the cart content in insertion order.
   *
   * @return products mapped to their quantities
   */
  public Map<Product, Integer> items() {
    return Collections.unmodifiableMap(items);
  }

  /**
   * Calculates the sum of all items before any discount.
   *
   * @return subtotal in cents
   */
  public long subtotalInCents() {
    long sum = 0;
    for (Map.Entry<Product, Integer> entry : items.entrySet()) {
      sum += entry.getKey().priceInCents() * entry.getValue();
    }
    return sum;
  }

  /**
   * Calculates the amount to pay after applying a discount.
   *
   * @param policy discount to apply
   * @return total in cents
   */
  public long totalInCents(DiscountPolicy policy) {
    long subtotal = subtotalInCents();
    return subtotal - policy.discountFor(subtotal);
  }
}
