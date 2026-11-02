# Zadaie 9

Tablicę deskryptorów może zmienić tylko sam proces. gdb zatrzymuje go i każe mu wywołać te same funkcje, których shell używa przy >: open, dup2, close.

## Komendy

```console
$ sleep 1000 &
[1] 4096
$ ls -l /proc/4096/fd
$ sudo gdb -q -p 4096
(gdb) set $fd = (int)open("/tmp/foo", 0102, 0644)
(gdb) print $fd
(gdb) print (int)dup2($fd, 1)
(gdb) print (int)close($fd)
(gdb) detach
(gdb) quit
$ ls -l /proc/4096/fd
```

sudo gdb -p 4096 podłącza się przez ptrace i zatrzymuje proces. sudo jest potrzebne, bo kernel.yama.ptrace_scope = 1. open("/tmp/foo", 0102, 0644) zwraca najniższy wolny deskryptor, a 0102 to O_RDWR (02) i O_CREAT (0100). Po dup2($fd, 1) fd 1 wskazuje na plik. close($fd) zamyka zbędny deskryptor, a detach odłącza gdb i proces działa dalej.

Rzutowanie (int) jest potrzebne, bo sleep nie ma informacji debugowych. Dla stdin używamy dup2($fd, 0), a dla stderr dup2($fd, 2).

## Test

Test na sleep 45, który uruchomił sam gdb, więc sudo nie było potrzebne. Stdin to /dev/null, a wyjście szło do orig2.log.

```
$1 = 3
$2 = 1
$3 = 0
[Inferior 1 (process 43460) detached]
```

```console
$ ls -l /proc/43460/fd
lrwx------ 1 kltpl kltpl 64 Nov  1 23:41 0 -> /dev/null
lrwx------ 1 kltpl kltpl 64 Nov  1 23:41 1 -> .../foo2
l-wx------ 1 kltpl kltpl 64 Nov  1 23:41 2 -> .../orig2.log
$ ps -o pid,stat,etime,cmd -p 43460
    PID STAT     ELAPSED CMD
  43460 Ss         00:02 sleep 45
```

Zmienił się tylko fd 1, a proces działał dalej. Przy pętli while :; do echo tick; sleep 1; done kolejne linie trafiały już do nowego pliku.
