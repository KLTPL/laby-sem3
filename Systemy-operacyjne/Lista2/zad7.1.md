# Zadanie 7.1

Pełne ślady: z7-gt.txt i z7-gtgt.txt.

```bash
strace -e trace=openat,close,dup2,fcntl,write bash -c 'echo ala > /tmp/a'
strace -e trace=openat,close,dup2,fcntl,write bash -c 'echo ala >> /tmp/a'
```

echo to wbudowane polecenie basha, więc shell sam wykonuje zapis, a potem przywraca swoje stdout. Wcześniejsze linie śladu to start basha.

## Ślad dla >

```
openat(AT_FDCWD, "/tmp/a", O_WRONLY|O_CREAT|O_TRUNC, 0666) = 3
fcntl(1, F_GETFD)                       = 0
fcntl(1, F_DUPFD, 10)                   = 10
fcntl(1, F_GETFD)                       = 0
fcntl(10, F_SETFD, FD_CLOEXEC)          = 0
dup2(3, 1)                              = 1
close(3)                                = 0
write(1, "ala\n", 4)                    = 4
dup2(10, 1)                             = 1
fcntl(10, F_GETFD)                      = 0x1 (flags FD_CLOEXEC)
close(10)                               = 0
```

1. openat otwiera plik jako fd 3.
2. fcntl(1, F_DUPFD, 10) zapisuje oryginalne stdout jako fd 10.
3. dup2(3, 1) kieruje fd 1 na plik, a close(3) zamyka zbędny deskryptor.
4. write(1, ...) zapisuje "ala\n" do pliku.
5. dup2(10, 1) i close(10) przywracają stdout.

## Różnica między > i >>

Ślady różnią się jedną linią:

```
>   openat(AT_FDCWD, "/tmp/a", O_WRONLY|O_CREAT|O_TRUNC, 0666) = 3
>>  openat(AT_FDCWD, "/tmp/a", O_WRONLY|O_CREAT|O_APPEND, 0666) = 3
```

- O_TRUNC przy otwarciu skraca istniejący plik do 0 bajtów
- O_APPEND zachowuje zawartość, a każdy write trafia na koniec pliku
