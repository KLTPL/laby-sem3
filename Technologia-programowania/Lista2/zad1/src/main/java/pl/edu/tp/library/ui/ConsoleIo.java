package pl.edu.tp.library.ui;

import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.ToLongFunction;

/**
 * Wczytywanie i wypisywanie tekstu w konsoli oraz wybór obiektu z listy.
 *
 * <p>GRASP Pure Fabrication i High Cohesion: szczegóły wejścia i wyjścia są w jednym
 * miejscu, więc {@link ConsoleUi} opisuje tylko przebieg przypadków użycia.
 */
public final class ConsoleIo {

  private final Scanner in;
  private final PrintStream out;

  /**
   * Tworzy obsługę konsoli.
   *
   * @param in strumień wejścia
   * @param out strumień wyjścia
   */
  public ConsoleIo(InputStream in, PrintStream out) {
    this.in = new Scanner(Objects.requireNonNull(in, "in"), StandardCharsets.UTF_8);
    this.out = Objects.requireNonNull(out, "out");
  }

  /**
   * Wypisuje pytanie i wczytuje odpowiedź, o ile wejście się nie skończyło.
   *
   * @param prompt pytanie
   * @return odpowiedź albo pusta wartość po końcu wejścia
   */
  public Optional<String> tryAsk(String prompt) {
    printPrompt(prompt);
    if (!in.hasNextLine()) {
      return Optional.empty();
    }
    return Optional.of(in.nextLine().trim());
  }

  /**
   * Wypisuje linię tekstu.
   *
   * @param text tekst
   */
  public void println(String text) {
    out.println(text);
  }

  /**
   * Wypisuje pytanie i wczytuje odpowiedź.
   *
   * @param prompt pytanie
   * @return odpowiedź bez spacji na brzegach
   */
  public String ask(String prompt) {
    printPrompt(prompt);
    return in.nextLine().trim();
  }

  private void printPrompt(String prompt) {
    out.print(prompt + ": ");
    out.flush();
  }

  /**
   * Wczytuje numer (identyfikator).
   *
   * @param prompt pytanie
   * @return numer
   * @throws IllegalArgumentException gdy odpowiedź nie jest liczbą
   */
  public long askId(String prompt) {
    String answer = ask(prompt);
    try {
      return Long.parseLong(answer);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("To nie jest numer: " + answer, e);
    }
  }

  /**
   * Wybiera jeden obiekt z listy: przy jednym trafieniu bez pytania, przy kilku pokazuje
   * listę z numerami i pyta o numer.
   *
   * @param matches pasujące obiekty
   * @param describe opis obiektu na liście
   * @param idOf numer obiektu
   * @param <T> typ obiektów
   * @return wybrany obiekt albo pusta wartość, gdy lista jest pusta
   * @throws IllegalArgumentException gdy podany numer nie jest na liście
   */
  public <T> Optional<T> choose(List<T> matches, Function<T, String> describe,
      ToLongFunction<T> idOf) {
    if (matches.isEmpty()) {
      println("Nic nie znaleziono.");
      return Optional.empty();
    }
    if (matches.size() == 1) {
      T only = matches.getFirst();
      println("Wybrano: [" + idOf.applyAsLong(only) + "] " + describe.apply(only));
      return Optional.of(only);
    }
    for (T item : matches) {
      println("  [" + idOf.applyAsLong(item) + "] " + describe.apply(item));
    }
    long id = askId("Numer");
    return Optional.of(matches.stream()
        .filter(item -> idOf.applyAsLong(item) == id)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Numeru " + id + " nie ma na liście")));
  }
}
