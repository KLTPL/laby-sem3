# Zadanie 4 – zarządzanie zadaniami

| Komenda | Co robi | Sygnał pod spodem |
|---|---|---|
| `cmd &` | uruchamia w tle; shell wypisuje `[nr] PID` | — |
| `jobs` / `jobs -l` | lista zadań shella (`-l` dodaje PID) | — |
| `Ctrl+Z` | **zatrzymuje** zadanie z pierwszego planu | `SIGTSTP` (wysyła go terminal) |
| `bg %n` | wznawia zatrzymane zadanie **w tle** | `SIGCONT` |
| `fg %n` | przenosi zadanie na **pierwszy plan** (i wznawia, jeśli było zatrzymane) | `SIGCONT` + zmiana TPGID |
| `kill %n` | kończy zadanie | `SIGTERM` (15) |
| `kill -9 %n` | zabija bezwarunkowo | `SIGKILL` (nie da się przechwycić) |
| `kill -STOP %n` / `kill -CONT %n` | zatrzymanie / wznowienie bez Ctrl+Z i bg | `SIGSTOP` / `SIGCONT` |
| `Ctrl+C` | przerywa zadanie z pierwszego planu | `SIGINT` |
