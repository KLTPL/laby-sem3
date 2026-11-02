# Zadanie 7.2

```bash
mkdir demo && touch demo/a demo/b
strace -e trace=openat,getdents64,statx ls -l demo
```

Fragmenty strace

```
openat(AT_FDCWD, "/etc/ld.so.cache", O_RDONLY|O_CLOEXEC) = 3
openat(AT_FDCWD, "/lib/x86_64-linux-gnu/libselinux.so.1", O_RDONLY|O_CLOEXEC) = 3
openat(AT_FDCWD, "/lib/x86_64-linux-gnu/libc.so.6", O_RDONLY|O_CLOEXEC) = 3
openat(AT_FDCWD, "/lib/x86_64-linux-gnu/libpcre2-8.so.0", O_RDONLY|O_CLOEXEC) = 3
openat(AT_FDCWD, "/proc/filesystems", O_RDONLY|O_CLOEXEC) = 3
openat(AT_FDCWD, "/usr/lib/locale/locale-archive", O_RDONLY|O_CLOEXEC) = 3
openat(AT_FDCWD, "/usr/share/locale/locale.alias", O_RDONLY|O_CLOEXEC) = 3
openat(AT_FDCWD, "/usr/lib/x86_64-linux-gnu/gconv/gconv-modules.cache", O_RDONLY|O_CLOEXEC) = 3
openat(AT_FDCWD, "/etc/nsswitch.conf", O_RDONLY|O_CLOEXEC) = 3
openat(AT_FDCWD, "/etc/passwd", O_RDONLY|O_CLOEXEC) = 3
openat(AT_FDCWD, "/etc/group", O_RDONLY|O_CLOEXEC) = 3
openat(AT_FDCWD, "demo", O_RDONLY|O_NONBLOCK|O_CLOEXEC|O_DIRECTORY) = 3
getdents64(3, 0x5b07f42336d0 /* 4 entries */, 32768) = 96
statx(AT_FDCWD, "demo/a", ) = 0
statx(AT_FDCWD, "demo/b", ) = 0
getdents64(3, 0x5b07f42336d0 /* 0 entries */, 32768) = 0
openat(AT_FDCWD, "/usr/share/locale-langpack/en/LC_MESSAGES/coreutils.mo", O_RDONLY) = 3
openat(AT_FDCWD, "/etc/localtime", O_RDONLY|O_CLOEXEC) = 3
```

/etc/ld.so.cache służy do wyszukania bibliotek współdzielonych, a libselinux.so.1, libc.so.6 i libpcre2-8.so.0 to biblioteki, z których korzysta ls. /proc/filesystems to lista systemów plików obsługiwanych przez jądro, którą sprawdza libselinux. locale-archive, locale.alias, gconv-modules.cache i coreutils.mo dotyczą języka, kodowania i tłumaczeń komunikatów. /etc/nsswitch.conf mówi, skąd brać dane użytkowników i grup, a /etc/passwd i /etc/group służą do zamiany UID i GID na nazwy. demo to listowany katalog, a /etc/localtime podaje strefę czasową dla daty modyfikacji.

Plików demo/a i demo/b ls nie otwiera. Potrzebuje tylko ich metadanych, które zwraca statx.

## /etc/ld.so.cache

Binarny indeks nazw bibliotek i ich ścieżek, generowany przez ldconfig. Czyta go linker dynamiczny ld.so przed startem main, dzięki czemu nie przeszukuje wszystkich katalogów z bibliotekami.

## getdents64

Wypełnia bufor wpisami katalogu: numer i-węzła, długość rekordu, typ i nazwa. Zwraca liczbę bajtów zapisanych w buforze.

- /* 4 entries */ ... = 96: 96 bajtów opisuje ., .., a i b.
- /* 0 entries */ ... = 0: wynik 0 oznacza koniec katalogu.
