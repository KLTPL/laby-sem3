package pl.edu.tp.cart;

/**
 * A product that can be put into a {@link Cart}.
 *
 * @param name human readable product name
 * @param priceInCents unit price in cents
 */
public record Product(String name, long priceInCents) {

  /**
   * Creates a product and validates its fields.
   */
  public Product {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("name must not be blank");
    }
    if (priceInCents < 0) {
      throw new IllegalArgumentException("price must not be negative");
    }
  }
}
