# Zadanie 7.3

```bash
head -c 200M /dev/urandom > duzy_plik
strace -c ls /usr/bin > /dev/null
strace -c -o cat.stats cat duzy_plik | cat > /dev/null
```

## ls /usr/bin

```
% time     seconds  usecs/call     calls    errors syscall
------ ----------- ----------- --------- --------- ----------------
 92,96    0,000647         161         4           getdents64
  1,72    0,000012           1         7           write
  1,58    0,000011           1         9           close
  1,44    0,000010          10         1           mremap
  1,29    0,000009           1         5           brk
  0,57    0,000004           0        19           mmap
------ ----------- ----------- --------- --------- ----------------
100,00    0,000696           8        87         6 total
```

## cat duzy_plik

```
% time     seconds  usecs/call     calls    errors syscall
------ ----------- ----------- --------- --------- ----------------
 57,22    0,031081          19      1600           write
 42,70    0,023192          14      1602           read
  0,06    0,000034          17         2           munmap
  0,02    0,000010           1         6           close
  0,00    0,000000           0         5           fstat
  0,00    0,000000           0        10           mmap
------ ----------- ----------- --------- --------- ----------------
100,00    0,054317          16      3246         1 total
```

## Porównanie

ls /usr/bin wykonuje 87 wywołań, a cat duzy_plik 3246. W ls czasowo dominuje getdents64, a najczęstsze jest mmap. W cat czasowo dominują read i write i są też najczęstsze: read 1602 razy, write 1600 razy.

ls głównie ładuje biblioteki, a najwięcej czasu zajmuje mu czytanie katalogu. cat to pętla read i write. Procenty zmieniają się między uruchomieniami.

## Rozmiar bufora cat

cat czyta porcjami po 131072 bajty, czyli 128 KiB:

```
openat(AT_FDCWD, "duzy_plik", O_RDONLY) = 3
fstat(3, {st_mode=S_IFREG|0664, st_size=209715200, ...}) = 0
fadvise64(3, 0, 0, POSIX_FADV_SEQUENTIAL) = 0
read(3, "\361\33#\220..."..., 131072) = 131072
write(1, "\361\33#\220..."..., 131072) = 131072
...
read(3, "", 131072)                     = 0
```

Liczby się zgadzają: 209715200 / 131072 = 1600 odczytów danych. Pozostałe 2 wywołania read to odczyt 832 bajtów nagłówka libc i końcowy odczyt zwracający 0 (koniec pliku).

fstat podaje rozmiar bloku (st_blksize, tutaj 4096), a GNU coreutils podnosi go do co najmniej 128 KiB. Większy bufor to mniej wywołań systemowych. POSIX_FADV_SEQUENTIAL informuje jądro, że plik będzie czytany po kolei.
