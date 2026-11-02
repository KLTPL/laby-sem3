# Zadanie 6: kolory ANSI

```bash
gcc -Wall -pedantic zad6.c -o zad6
./zad6
```

Program wypisuje "Hello, World!" z użyciem sekwencji SGR: ESC [ parametry m. ESC to bajt 0x1B, w C \033. ESC[0m przywraca domyślny kolor.

## Czy terminal może wyświetlić 256 lub więcej kolorów?

Tak, jeśli go obsługuje. Kolory podstawowe i jasne (8 + 8) to ESC[30m..ESC[37m i ESC[90m..ESC[97m. Paleta 256 kolorów to ESC[38;5;Nm. Truecolor (24 bity, 16 777 216 kolorów) to ESC[38;2;R;G;Bm.

Paleta 256 to 16 kolorów podstawowych (0-15), sześcian RGB 6x6x6 (16-231) i 24 odcienie szarości (232-255).

Sprawdzenie na tym komputerze:

```console
$ tput colors
256
$ echo $COLORTERM
truecolor
```

tput colors czyta wpis terminfo dla $TERM, a truecolor zgłasza zmienna COLORTERM.

Program wypisuje całą paletę 256 kolorów. Z truecolor wypisuje tylko próbkę 216 kolorów (krok 51 na kanał), bo wszystkich jest prawie 17 milionów.
