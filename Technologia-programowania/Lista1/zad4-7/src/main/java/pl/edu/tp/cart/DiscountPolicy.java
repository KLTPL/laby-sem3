package pl.edu.tp.cart;

/**
 * Strategy that calculates a discount for a given cart subtotal.
 */
public interface DiscountPolicy {

  /**
   * Calculates the discount.
   *
   * @param subtotalInCents cart subtotal before any discount, in cents
   * @return discount in cents, never negative and never above the subtotal
   */
  long discountFor(long subtotalInCents);
}
