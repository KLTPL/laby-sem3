package pl.edu.tp.cart;

/**
 * Discount that takes a fixed percentage off the cart subtotal.
 */
public final class PercentageDiscount implements DiscountPolicy {

  private static final int MAX_PERCENT = 100;

  private final int percent;

  /**
   * Creates a discount.
   *
   * @param percent discount size, between 0 and 100 inclusive
   */
  public PercentageDiscount(int percent) {
    if (percent < 0 || percent > MAX_PERCENT) {
      throw new IllegalArgumentException("percent must be between 0 and 100");
    }
    this.percent = percent;
  }

  @Override
  public long discountFor(long subtotalInCents) {
    return subtotalInCents * percent / MAX_PERCENT;
  }
}
