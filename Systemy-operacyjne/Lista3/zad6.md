# Zadanie 6 — colors in xv6

Only changed files are in the repo (`zad6/xv6/console.c`, `hello.c`, `Makefile`).
The rest of xv6 is ignored by `.gitignore` and has to be fetched again.

## Restore after cloning

```bash
cd Lista3/zad6
mv xv6 xv6-mine
git clone https://github.com/mit-pdos/xv6-public.git xv6
rm -rf xv6/.git
wget https://cs.pwr.edu.pl/zawada/so/xv6.patch
patch -d xv6 -p1 -i ../xv6.patch
rm xv6.patch
cp xv6-mine/* xv6/ && rm -rf xv6-mine
```

## Run

```bash
cd xv6
make qemu          # VGA window shows colors (qemu-nox: terminal interprets ANSI itself)
$ hello
```

## How it works

- Programs print ANSI escape sequences, e.g. `\033[31m` (red), `\033[0m` (reset).
- `console.c`: `ansiputc()` is a small state machine in front of `cgaputc()`:
  `ESC` -> `[` -> digits -> `m` (or `;` for the next code). Escape characters are not printed.
- `setcolor()` changes the current attribute byte: `30-37` foreground, `90-97` bright
  foreground, `40-47` background, `0` reset.
- CGA text memory (`0xb8000`) stores 2 bytes per character: low = ASCII, high = attribute
  (bits 0-3 foreground, 4-7 background). So `crt[pos] = c | (color << 8)`.
- ANSI and CGA color order differ (ANSI 1 = red, CGA 4 = red), hence the `ansi2cga` table.
- `hello.c` prints `Hello, World` in all 16 colors; `Makefile`: `_hello` in `UPROGS`, `hello.c` in `EXTRA`.
