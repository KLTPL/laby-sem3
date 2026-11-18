# Zadanie 5 — `expr` for xv6

Only the files changed for this task are in the repo (`zad5/xv6/expr.c`, `zad5/xv6/Makefile`).
The rest of xv6 is ignored by `.gitignore` and has to be fetched again.

## Restore after cloning

```bash
cd Lista3/zad5
mv xv6 xv6-mine                      # keep our files aside
git clone https://github.com/mit-pdos/xv6-public.git xv6
rm -rf xv6/.git
wget https://cs.pwr.edu.pl/zawada/so/xv6.patch
patch -d xv6 -p1 -i ../xv6.patch     # lecturer's patch (builds with recent gcc)
rm xv6.patch
cp xv6-mine/* xv6/ && rm -rf xv6-mine # put expr.c and modified Makefile back
```

## Run

```bash
cd xv6
make qemu-nox      # exit: Ctrl+A, then x
```

```
$ expr 2 + 2
4
$ expr 10 / 3
3
```

## Changes

- `expr.c`: parses `num op num`, supports `+ - * / %`, negative numbers, division by zero check.
- `Makefile`: `_expr` added to `UPROGS` (installs it into `fs.img`), `expr.c` added to `EXTRA`.
