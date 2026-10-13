
  package pl.edu.tp.cart;

  import java.util.List;

  public class Bad {
      private int unused = 5;

      public void doIt(int a) {
          int b = 3;
          if (a == 1) {
          }
          try {
              Integer.parseInt("x");
          } catch (Exception e) {
          }
      }
  }
