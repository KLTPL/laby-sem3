# Zadanie 8

Skrypt: zad8.sh. Potok: find podaje nazwy, a pętla while read zmienia je przez mv.

```bash
find . -mindepth 1 -maxdepth 1 -type f -printf '%f\0' |
    while IFS= read -r -d '' name; do ...
```

## Pokaz

Nazwy zaczynające się od - wymagają -- także w touch:

```console
$ mkdir test && cd test
$ touch -- " ABC DEF" "-AbC aBc" "--ABC DEF"
$ ls -Q
"-AbC aBc"  " ABC DEF"  "--ABC DEF"
$ ../zad8.sh .
$ ls -Q
"-abc abc"  " abc def"  "--abc def"
```

## Opis działania

-mindepth 1 -maxdepth 1 ogranicza wyniki do tego katalogu, bez podkatalogów, a -type f do zwykłych plików. -printf '%f\0' i read -d '' rozdzielają nazwy bajtem NUL, którego nie ma w żadnej nazwie. IFS= zachowuje spację na początku " ABC DEF", a read -r ukośniki odwrotne. Dzięki mv -- nazwa -AbC aBc nie jest traktowana jak opcja. ${name,,} zamienia litery według locale, także polskie (tr działa na bajtach).

Gdy istnieją KOLIZJA i kolizja, skrypt pomija plik i wypisuje komunikat na stderr, więc niczego nie nadpisuje.

## Test

ls -Q po uruchomieniu na katalogu z wymaganymi nazwami i przypadkami brzegowymi:

```
"-abc abc"  " abc def"  "--abc def"  "already lower"  ".hidden"  "kolizja"  "KOLIZJA"  "new\nline"  "Sub Dir"  "zażółć.txt"
```

Plik Sub Dir/KEEP w podkatalogu nie zmienił nazwy.
