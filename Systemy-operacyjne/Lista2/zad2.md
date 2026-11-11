# Zad2

## Szukanie czy proces istnieje

```bash
ps -p 4384 # po PID
ps -C zsh # po nazwie programu
```

## Wyświetlanie szczegółów procesu

```bash
ps -o pid,ppid,stat,ni,pri,rss,vsz,%mem,%cpu,time,etime,tty,cmd -p 4384
```

## Wypisanie procesów związanych z terminalem

```bash
tty
```

```
ps axjf | grep pts/
```
