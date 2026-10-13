package pl.edu.tp.cart;

/**
 * Entry point that builds a sample cart and prints its receipt.
 */
public final class App {

  private App() {
  }

  /**
   * Runs the demo.
   *
   * @param args ignored
   */
  public static void main(String[] args) {
    Cart cart = new Cart();
    cart.add(new Product("Notebook", 1299), 2);
    cart.add(new Product("Pen", 250), 5);
    cart.add(new Product("Backpack", 8999), 1);

    DiscountPolicy policy = new PercentageDiscount(10);
    System.out.print(new ReceiptPrinter().render(cart, policy));
  }
}
