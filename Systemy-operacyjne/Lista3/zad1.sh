#!/bin/bash
# Simple htop-like process manager using /proc.

shopt -s nullglob

cleanup() {
  stty sane
  tput cnorm
  tput clear
}
trap cleanup EXIT

# Decode tty_nr (from /proc/PID/stat) into a name like pts/3 or tty1.
tty_name() {
  local nr=$1
  local major=$(((nr >> 8) & 0xff))
  local minor=$(((nr & 0xff) | ((nr >> 12) & 0xfff00)))
  case $major in
  0) TTY="?" ;;
  4) TTY="tty$minor" ;;
  136) TTY="pts/$minor" ;;
  *) TTY="$major:$minor" ;;
  esac
}

show_processes() {
  local rows=$(($(tput lines) - 3))
  local count=0 pid line comm rest fds

  tput cup 0 0
  printf "%-8s %-8s %-16s %-5s %-8s %-10s %-8s %-8s %-5s\033[K\n" \
    PPID PID COMM STATE TTY "RSS(kB)" PGID SID FILES

  # newest (highest PID) first; glob alone sorts PIDs as text
  for dir in $(printf '%s\n' /proc/[0-9]* | sort -t/ -k3 -nr); do
    [[ -O $dir ]] || continue # only current user's processes
    read -r line <"$dir/stat" 2>/dev/null || continue
    pid=${line%% *}
    comm=${line#*(}
    comm=${comm%)*}     # name between first "(" and last ")"
    rest=(${line##*) }) # fields after ") ": state ppid pgrp sid tty ...
    tty_name "${rest[4]}"
    fds=("$dir"/fd/*)

    printf "%-8s %-8s %-16.16s %-5s %-8s %-10s %-8s %-8s %-5s\033[K\n" \
      "${rest[1]}" "$pid" "$comm" "${rest[0]}" "$TTY" \
      $((rest[21] * 4)) "${rest[2]}" "${rest[3]}" "${#fds[@]}"

    ((++count >= rows)) && break
  done
  tput ed # clear leftover lines below the list
  printf "\n[q] quit  [s] stop  [c] continue  [t] term  [k] kill  [r] renice"
}

# Ask for input in normal (canonical) terminal mode.
ask() {
  stty echo icanon
  tput cnorm
  printf "\n"
  read -rp "$1" REPLY
  stty -echo -icanon
  tput civis
}

tput civis
tput clear
stty -echo -icanon

while true; do
  show_processes
  read -rsn1 -t 1 key || continue
  case $key in
  q) break ;;
  s)
    ask "PID to stop: "
    kill -STOP "$REPLY"
    ;;
  c)
    ask "PID to continue: "
    kill -CONT "$REPLY"
    ;;
  t)
    ask "PID to terminate: "
    kill -TERM "$REPLY"
    ;;
  k)
    ask "PID to kill: "
    kill -KILL "$REPLY"
    ;;
  r)
    ask "PID to renice: "
    pid=$REPLY
    ask "New nice value: "
    renice -n "$REPLY" -p "$pid"
    ;;
  esac
  sleep 1 # let user see kill/renice result
done
