# Lista 3: wzorce projektowe (raport na prezentację)

Wszystkie ścieżki są liczone od `Lista3/designpatterns/<moduł>/src/`. Przedrostek pakietu `eu/jpereira/trainings/designpatterns/` pomijam.
Wszystkie testy przechodzą (`mvn test` w każdym module). Zmiany w kodzie: `git diff 0c3993a -- Lista3/designpatterns`.

**Wspólne dla Zad2, Zad4 i Zad5.** Oryginał łamał dwie zasady:
- **OCP** (open/closed): dodanie nowego formatu wymagało dopisania kolejnego `if`;
- **DIP** (dependency inversion): klasa wysokiego poziomu tworzyła konkretne klasy przez `new`.

Każdy wzorzec usuwa ten problem w inny sposób. Na prezentacji trzeba umieć wskazać tę różnicę.

| Wzorzec | Kto wybiera konkretną klasę? |
|---|---|
| Builder (Zad2) | wywołujący przekazuje builder jako **parametr** |
| Factory Method (Zad4) | **podklasa** nadpisuje metodę fabrykującą |
| Abstract Factory (Zad5) | wywołujący przekazuje **fabrykę całej rodziny** obiektów |

---

## Zad1: Adapter (`adapter`)

- `ThirdPartyDoor` (kod producenta, którego nie wolno zmieniać) nie implementuje `Door`. Adapter tłumaczy jedno na drugie.
- **Wariant klasowy** `ThirdPartyDoorAdaper` (nazwa z literówką, bo tak jest w teście) to `extends ThirdPartyDoor implements Door`, czyli IS-A.
- **Wariant obiektowy** `ThirdPartyDoorObjectAdapter` ma prywatne pole `ThirdPartyDoor`, czyli HAS-A (wskazówka prowadzącego).
- Najważniejsze elementy:
  - wyjątek `CannotUnlockDoorException` zamieniam na `IncorrectDoorCodeException`;
  - `changeCode` sprawdza potwierdzenie kodu **przed** zmianą stanu;
  - `changeCode` i `testCode` przywracają początkowy `lockStatus`, dzięki czemu zawsze zachodzi „LOCKED ⇒ CLOSED”.
- **Pytanie na obronie: czym różnią się warianty?** Klasowy wystawia na zewnątrz metody producenta, np. `lock()`, więc można ominąć logikę adaptera. Obiektowy je ukrywa. Klasowy zużywa jedyne dziedziczenie, na jakie pozwala Java.

## Zad2: Builder (`builder`)

- Usunąłem `if` po typie z `ReportAssembler`.
- `ReportBodyBuilder` to klasa abstrakcyjna z krokami `addCustomer`, `addItems`, `getReportBody`.
- Implementacje: `JSONReportBuilder`, `XMLReportBuilder`, `HTMLReportBuilder`.
- `ReportAssembler.getReport(ReportBodyBuilder)` pełni rolę **Directora**: wywołuje kroki zawsze w tej samej kolejności, a format zależy od przekazanego buildera.
- W teście jest `getReport(new JSONReportBuilder())` zamiast `"JSON"`. Oczekiwane napisy się nie zmieniły, co dowodzi, że zachowanie jest takie samo.

## Zad3: Singleton (`singleton`)

- `ReportBuilder` ma **prywatny konstruktor** i pole `private static volatile ReportBuilder instance`.
- `getInstance()` używa **double-checked locking**: sprawdzenie bez blokady → `synchronized (ReportBuilder.class)` → drugie sprawdzenie → `new`.
- `getInstanceSynchronized()` to wariant z `synchronized` na całej metodzie, dodany do porównania.
- Pomiar (test `compareSynchronizedVsDoubleCheckedLocking`, 10 wątków × 1 000 000 wywołań): `synchronized` **522 ms**, DCL **27 ms**.
- **Pytania na obronie:**
  - *Po co drugie sprawdzenie?* Wątek, który czekał na blokadę, utworzyłby drugi obiekt.
  - *Po co `volatile`?* Bez niego inny wątek mógłby zobaczyć referencję do obiektu, którego konstruktor jeszcze się nie skończył (procesor i JVM mogą zmienić kolejność zapisów).
  - *Dlaczego oryginał failował?* Wyścig „sprawdź, potem utwórz”: wszystkie wątki widziały `null`, bo konstruktor trwał kilka sekund.

## Zad4: Factory Method (`factorymethod`)

| Miejsce | Co tam jest |
|---|---|
| `creational/factorymethod/ReportGenerator.java:22` | `ReportGenerator` jest teraz `abstract`, czyli **Creator** |
| `:24–25` | wspólny algorytm `generateReport(ReportData)`: `createReport()` → `report.generateReport(data)` |
| `:33` | `protected abstract Report createReport()`, czyli **metoda fabrykująca** |
| nowe klasy | `JSONReportGenerator`, `XMLReportGenerator`, `HTMLReportGenerator`, `PDFReportGenerator` (**ConcreteCreatorzy**), każda zwraca odpowiednie `new XxxReport()` |
| test | `new JSONReportGenerator().generateReport(data)`: parametr `String type` zniknął |

- `ReportGenerator` nie ma już ani jednego `if` (wskazówka prowadzącego).
- Nowy format raportu oznacza nową podklasę, bez zmian w `ReportGenerator`.
- **Pytanie na obronie: czym różni się od Buildera i Abstract Factory?** Factory Method tworzy **jeden** produkt, a wybór następuje przez **dziedziczenie**, nie przez przekazanie obiektu.

## Zad5: Abstract Factory (`abstractfactory`)

| Miejsce | Co tam jest |
|---|---|
| `creational/abstractfactory/ReportFactory.java` | nowy interfejs: `createHeader()`, `createBody()`, `createFooter()` |
| `json/JSONReportFactory.java`, `xml/XMLReportFactory.java` | fabryki konkretne, każda tworzy **spójną rodzinę** obiektów (same JSON albo same XML) |
| `Report.java:31–35` | `Report(ReportFactory factory)`: konstruktor bez `if`, bez importów `json.*`/`xml.*` i bez pola `reportType` |
| test | `new Report(new JSONReportFactory())` |

- **Pytanie na obronie: dlaczego Abstract Factory, a nie trzy fabryki?** Gwarantuje, że nagłówek, treść i stopka pochodzą z tej samej rodziny. Nie da się pomieszać nagłówka JSON ze stopką XML.

## Zad6: Composite (`composite`)

Zmieniłem tylko klasy `Shape` i `CompositeShape`, zgodnie ze wskazówką.

| Miejsce | Co tam jest |
|---|---|
| `structural/composite/model/Shape.java:34` | `asComposite()` zwraca `null` dla liści (taki kontrakt podaje javadoc) |
| `CompositeShape.java:115–116` | nadpisanie `asComposite()` → `return this`: **polimorfizm zamiast `instanceof`** |
| `Shape.java:50–54` | `move()` przesuwa obiekt, a jeśli jest kompozytem, rekurencyjnie wywołuje `move` na dzieciach. Przesunięcie całej hierarchii to jedno wywołanie. |
| `CompositeShape.java:43–70` | `addShape`, `removeShape` i `getShapeCount` delegują do listy. `getShapes()` zwraca **widok tylko do odczytu** (`Collections.unmodifiableList`), więc dzieci zmienia się wyłącznie przez `add`/`remove`. |
| `:77` | `getShapesByType` filtruje bezpośrednie dzieci |
| `:92–99` | `getLeafShapes` rekurencyjnie zbiera liście z całego drzewa |
| `createShapesList()` | zwraca `new ArrayList` (to też metoda fabrykująca) |

- Klient traktuje pojedynczy kształt i grupę kształtów tak samo (`Shape`). Na tym polega Composite.

## Zad7: State (`state`)

Wykonałem TODO w kolejności z PDF.

| Miejsce | Co tam jest |
|---|---|
| `behavioral/state/appliance/state/ApplianceState.java:26–50` | odkomentowane stałe enuma z własnymi ciałami: każda stała tworzy swój obiekt stanu (`OFF` → `new OffState()` itd.) |
| `appliance/Toaster.java:69, 83, 97, 111` | `this.applianceStateBehavior = this.applianceStateBehavior.turnOn()` itd. **Toster nie ma żadnego `if`/`switch` po stanie.** Bieżący obiekt stanu decyduje, jaki będzie następny. |
| `OffState.java:41, 52` | `start` i `stop` w stanie OFF → `return this` (przejście niedozwolone, stan bez zmian) |
| `OffState.java:62` | `turnOn` → `ApplianceState.ON.getStateBehavior()` |
| `OnState.java:29, 37` | `getState()` → `ON`; `start()` → STARTED |
| `StartedState.java:29` | `getState()` → `STARTED` |

- Automat stanów: OFF → ON → STARTED → STOPPED → OFF.
- **Pytanie na obronie: co zyskujemy?** Nowy stan to nowa klasa. Logika przejść jest rozproszona po stanach, a nie zebrana w jednym wielkim `switch`.

## Zad8: Decorator (`decorator`)

Nowy dekorator **bez zmiany istniejącego kodu**: `structural/decorator/channel/decorator/WordCensor.java`.

| Linijki | Co robi |
|---|---|
| `:13` | `extends SocialChannelDecorator`, tak jak `MessageTruncator` i `URLAppender` |
| `:21` | każde słowo do cenzury zamieniam na wzorzec `\bsłowo\b`. Cenzuruję **tylko całe słowa**: „Microsofty” zostaje bez zmian. `Pattern.quote` zabezpiecza przed znakami specjalnymi regexa. |
| `:34` | słowo zamieniam na `###` |
| `:36` | przekazuję zmienioną wiadomość dalej (`delegate.deliverMessage`), czyli **istota Decoratora** |

Testy:
- `WordCensorTest` (6 testów): jedno słowo, każde wystąpienie, kilka słów, wiadomość bez słów do cenzury, fragment innego słowa, użycie bez buildera;
- `ChainCensorDecoratorTest` (2 testy): łańcuch WordCensor → MessageTruncator(15) → URLAppender, z builderem i bez. `"Microsoft Windows is great!!"` daje `"### Windows ... http://jpereira.eu"`.

**Kolejność w łańcuchu ma znaczenie.** Cenzura idzie przed obcinaniem, więc obcinanie liczy długość już ocenzurowanego tekstu.

**Raport Cobertura (punkt 6) nie został wygenerowany.** Cobertura nie działa z Javą 8+, a dodanie JaCoCo oznaczałoby nową wtyczkę w `pom.xml`. `WordCensor` ma tylko jedną ścieżkę wykonania i testy ją pokrywają.

## Zad9: Facade (`facade`)

| Miejsce | Co tam jest |
|---|---|
| `structural/facade/DefaultBookstoreFacade.java:16` | `implements BookstoreFacade`. Opis zadania podaje nazwę `DefaultBooktoreFacade` (literówka), ja użyłem poprawnej pisowni. |
| `:25–33` | `placeOrder(customerId, isbn)` to **jedno wywołanie** zamiast pięciu serwisów: znajdź klienta → znajdź książkę → utwórz zamówienie → powiadom → wyślij z magazynu → powiadom o wysyłce |
| setery | zależności (serwisy) wstrzykuje się przez setery |
| test `BookStoreFacadeTest.java:68` | `createFacade()` wstrzykuje mocki Mockito |
| test `:58–59` | **`verify`** sprawdza, czy fasada naprawdę wywołała `warehouseService.dispatch(order)` i `notifyClient(dispatchReceipt)`, i to z dokładnie tymi obiektami, które zwróciły wcześniejsze serwisy (wskazówka prowadzącego) |

- **Pytanie na obronie: czym `verify` różni się od `assertEquals`?** `assertEquals` sprawdza **wynik**, a `verify` sprawdza **interakcję**: czy dana metoda mocka została wywołana i z jakimi argumentami. `placeOrder` zwraca `void`, więc tylko tak da się przetestować jej zachowanie.

## Zad10: dwa dodatkowe zadania

PDF nie mówi, które zadania wybrać. Wybrałem dwa z gotową instrukcją.

**1. Command (`command`)**, według wiki projektu (`CommandPattern.wiki`) i `QueryInstanceStatusCommandTest`:
- nowy interfejs `behavioral/command/model/command/Command.java`: `execute()` i `getResult()`;
- `DBServerInstanceCommand` i `DBServerInstanceGroupCommand` dostały `implements Command`, a Start, Stop i Query odkomentowane `execute`/`getResult`;
- `CommandJob` (`:33`) to **Composite poleceń**: `execute()` uruchamia każde dodane polecenie (`:63`);
- `CommandFactory`, `CommandScheduler.schedule(Command, Schedule)` i `SpyCommandScheduler` są odkomentowane;
- `ReferenceDBServerInstanceService`: serwis tworzy polecenie z fabryki (`:112`) i przekazuje je do schedulera (`:114`), nie wywołując samej operacji bezpośrednio. Zapytanie o status wykonuje synchronicznie (`:149`);
- usunąłem test `deleteThisTest`, zgodnie z jego treścią.
- **Istota wzorca:** żądanie jest obiektem, więc można je zaplanować, zebrać w kolejkę albo zgrupować (`CommandJob`).

**2. Decorator 1.3 (opcjonalne): nowy kanał Google+** bez zmiany istniejącego kodu:
- `GooglePlusChannel` (`implements SocialChannel`);
- `GooglePlusSocialChannelBuilder extends DefaultSocialChannelBuilder`: nadpisuje `addDefaultChannels()`, woła `super` (`:10`) i rejestruje nowy kanał (`:11`);
- `GooglePlusChannelTest` (3 testy), w tym dekorowanie nowego kanału cenzurą i obcinaniem;
- **pokazuje OCP:** dodałem funkcję wyłącznie nowymi klasami, nie zmieniając żadnej istniejącej.

## Zmiany techniczne poza zadaniami

- Wszystkie 21 plików `pom.xml`: mockito 1.9.0-rc1 → 3.2.4 i source/target 1.6 → 1.8 (zgodnie z PDF). Bez tego testy nie działają na nowym JDK.
