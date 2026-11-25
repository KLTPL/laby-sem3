#!/bin/bash
# Real-time directory monitor using inotifywait (inotify-tools).
# Usage: ./zad3.sh [directory]

DIR=${1:-.} # use $1 or default to .
LOG=monitor.log

GREEN='\033[32m'
RED='\033[31m'
YELLOW='\033[33m'
BLUE='\033[34m'
RESET='\033[0m'

declare -A count=([created]=0 [deleted]=0 [modified]=0 [renamed]=0) # create a Map

print_stats() {
  printf -- "--- stats: created %d, deleted %d, modified %d, renamed %d ---\n" \
    "${count[created]}" "${count[deleted]}" "${count[modified]}" "${count[renamed]}"
}

echo "Watching $DIR (Ctrl+C to quit)"
last_stats=$SECONDS

# -m: keep running, -r: recursive, -q: no startup messages.
# monitor.log is excluded so writing the log does not trigger new events.
while true; do
  if read -r -t 1 event path; then
    case $event in
      CREATE*)     color=$GREEN  op=created ;;
      DELETE*)     color=$RED    op=deleted ;;
      MODIFY*)     color=$YELLOW op=modified ;;
      MOVED_FROM*) color=$BLUE   op=renamed path="$path ->" ;;
      MOVED_TO*)   color=$BLUE   op=renamed ;;
      *) continue ;;
    esac
    # a rename gives two events (FROM and TO); count it once
    [[ $event == MOVED_FROM* ]] || ((count[$op]++))

    printf -v now '%(%Y-%m-%d %H:%M:%S)T' -1   # date without forking
    echo -e "${color}[$now] $op: $path${RESET}"
    echo "[$now] $op: $path" >>"$LOG"
  elif (($? <= 128)); then
    break                               # inotifywait ended (EOF), not a timeout
  fi

  if ((SECONDS - last_stats >= 10)); then
    print_stats
    last_stats=$SECONDS
  fi
done < <(inotifywait -mrq -e create,delete,modify,move \
  --exclude "$LOG" --format '%e %w%f' "$DIR") # -m: monitor works infinitely, -r recursive, -quiet to silien
# %e - event name, %w - directory, %f - file
