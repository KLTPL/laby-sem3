# Zadanie 7.4

## Jeden proces

```bash
sleep 300 &
strace -e trace=openat,read,close -o ps.trace ps -p $! -o pid,ppid,stat,comm
grep -n -A2 -E "/proc/$!/(stat|status)\"" ps.trace
```

Wynik ps:

```
    PID    PPID STAT COMMAND
  43103   43058 S    sleep
```

Linie śladu:

```
4434:openat(AT_FDCWD, "/proc/43103/stat", O_RDONLY) = 4
4435-read(4, "43103 (sleep) S 43058 43056 4305"..., 2048) = 311
4436-close(4)                                = 0
4437:openat(AT_FDCWD, "/proc/43103/status", O_RDONLY) = 4
4438-read(4, "Name:\tsleep\nUmask:\t0002\nState:\tS"..., 2048) = 1543
4439-close(4)                                = 0
```

Początek odczytanych danych to dokładnie kolumny wypisane przez ps: PID 43103, nazwa (sleep), stan S, PPID 43058.

## Wszystkie procesy

```bash
strace -e trace=openat,getdents64 -o psall.trace ps -e > /dev/null
```

```
43:openat(AT_FDCWD, "/proc", O_RDONLY|O_NONBLOCK|O_CLOEXEC|O_DIRECTORY) = 3
44:getdents64(3, 0x5e6fe0266a10 /* 435 entries */, 32768) = 11208
45:openat(AT_FDCWD, "/proc/1/stat", O_RDONLY) = 4
50:openat(AT_FDCWD, "/proc/2/stat", O_RDONLY) = 4
```

ps -e listuje katalog /proc przez getdents64 i czyta plik stat każdego procesu. W tym śladzie jest 371 otwarć plików stat.
