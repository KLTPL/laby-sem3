# zad4-7

## Launch

```bash
mvn clean verify
java -jar target/cart-app-1.0-SNAPSHOT.jar
```

## zad 4

```bash
mvn pmd:check
mvn pmd:pmd
```

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-pmd-plugin</artifactId>
  <version>3.28.0</version>
</plugin>
```

## zad 5

```bash
mvn checkstyle:check

mvn checkstyle:checkstyle
```

## zad 6

UML class diagram: [`docs/class-diagram.md`](docs/class-diagram.md), written in Mermaid.
GitHub renders it; in Neovim use `:MarkdownPreview`.

| Notation | Meaning |
|---|---|
| `A <\|-- B` | B extends A (inheritance) |
| `A <\|.. B` | B implements interface A |
| `A o-- B` | aggregation: A has B, B can live on its own |
| `A *-- B` | composition: B cannot exist without A |
| `A --> B` | association: A holds a reference to B |
| `A ..> B` | dependency: A uses B temporarily |

## zad 7

Cart application: `Cart`, `Product`, `DiscountPolicy`, `PercentageDiscount`,
`ReceiptPrinter`, `App`. Uses Maven, Git, PMD, Checkstyle and UML.

### Demo script

| Step | Do | Say |
|---|---|---|
| Maven | `mvn clean verify` | one command: compile, tests, PMD, Checkstyle |
| PMD | add `Bad.java`, `mvn pmd:check` | 5 violations, report in `target/pmd.xml` |
| Checkstyle | break indentation, `mvn checkstyle:check` | Google style, 2 spaces |
| UML | open `docs/class-diagram.md` | matches the code |
| Git | `git log --oneline` | commit history |

### Be ready to change live

Typical request: add a new `DiscountPolicy` (e.g. a fixed amount discount).
Write the class, a test, run `mvn clean verify`, update the diagram.

## Cheat sheet

- **Lifecycle:** `validate` → `compile` → `test` → `package` → `verify` → `install` → `deploy`.
  Calling a phase runs all earlier phases.
- **Phase vs goal:** a phase is a step of the lifecycle (`package`); a goal is a task of a
  plugin (`compiler:compile`, `pmd:check`). Plugins bind goals to phases.
- **PMD vs Checkstyle:** PMD finds bad practices and likely bugs (unused code, empty
  `catch`); Checkstyle enforces formatting and naming conventions.
- **Static analysis:** inspects source code without running it, unlike tests.
- **`<scope>test</scope>`:** dependency available only for tests, not packaged.
- **`SNAPSHOT`:** version still under development.
- **`~/.m2/repository`:** local cache of downloaded dependencies and installed artifacts.
- **Inheritance vs aggregation (POM):** `<parent>` inherits configuration; `<modules>`
  lists projects that are built together. One POM can do both.

## Suppressing false positives

```java
@SuppressWarnings("PMD.UnusedPrivateField")
private int reserved;

int x = compute(); // NOPMD
```

## Reports

| Tool | Report |
|---|---|
| PMD | `target/pmd.xml` |
| Checkstyle | `target/checkstyle-result.xml` |
| Tests | `target/surefire-reports/` |

## Gotchas

- Use `maven-pmd-plugin` 3.28.0 or newer on JDK 25; older versions flood the log with
  `Parsing failed` errors.
- `sun.misc.Unsafe` warnings printed by Maven 3.8.7 are harmless.
- When pasting XML from a terminal, make sure `<?xml` is the very first line of the file.
- `mv` silently overwrites existing files; use `mv -i`.
- Run `mvn` from the directory that contains the right `pom.xml`.
