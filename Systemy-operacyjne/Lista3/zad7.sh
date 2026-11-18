#!/bin/bash
# Conway's Game of Life in the terminal.
# Usage: ./zad7.sh            random board
#        ./zad7.sh file.txt   pattern from file ('o' or '#' = alive), centered

ROWS=$(($(tput lines) - 2))   # 2 lines left for HUD
COLS=$(tput cols)
delay=200                     # ms between generations
paused=0
gen=0

GREEN=$'\033[32m'
YELLOW=$'\033[93m'            # newly born cells
RESET=$'\033[0m'

cleanup() {
  stty sane
  tput cnorm
  tput clear
}
trap cleanup EXIT

# Board is a flat array: cell (r, c) has index r*COLS + c, value 1 = alive.
# old[] keeps the previous generation (to highlight new cells).
init_random() {
  local i
  for ((i = 0; i < ROWS * COLS; i++)); do
    board[i]=$((RANDOM % 4 == 0))   # ~25% alive
    old[i]=1
  done
}

init_file() {
  local i line r=0 c
  for ((i = 0; i < ROWS * COLS; i++)); do board[i]=0 old[i]=1; done
  mapfile -t lines <"$1"
  local top=$(((ROWS - ${#lines[@]}) / 2)) left=$(((COLS - ${#lines[0]}) / 2))
  ((top < 0)) && top=0
  ((left < 0)) && left=0
  for line in "${lines[@]}"; do
    for ((c = 0; c < ${#line} && left + c < COLS; c++)); do
      [[ ${line:c:1} == [o#] ]] && board[(top + r) * COLS + left + c]=1
    done
    ((++r + top >= ROWS)) && break
  done
}

# Neighbor column/row offsets with wrap-around (board is a torus).
# Precomputed once so the main loop does only one arithmetic expression per cell.
for ((c = 0; c < COLS; c++)); do
  LEFT[c]=$(((c - 1 + COLS) % COLS)) RIGHT[c]=$(((c + 1) % COLS))
done
for ((r = 0; r < ROWS; r++)); do
  UP[r]=$((((r - 1 + ROWS) % ROWS) * COLS)) DOWN[r]=$((((r + 1) % ROWS) * COLS))
done

step() {
  local r c rc u d l rt n i
  local -a next
  alive=0
  for ((r = 0; r < ROWS; r++)); do
    rc=$((r * COLS)) u=${UP[r]} d=${DOWN[r]}
    for ((c = 0; c < COLS; c++)); do
      l=${LEFT[c]} rt=${RIGHT[c]} i=$((rc + c))
      n=$((board[u + l] + board[u + c] + board[u + rt] + board[rc + l] + board[rc + rt] + board[d + l] + board[d + c] + board[d + rt]))
      # born with 3 neighbors, survives with 2 or 3
      next[i]=$((n == 3 || (n == 2 && board[i])))
      ((alive += next[i]))
    done
  done
  old=("${board[@]}")
  board=("${next[@]}")
  ((gen++))
}

draw() {
  local r c i frame="" row p=""
  ((paused)) && p="  [PAUSED]"
  for ((r = 0; r < ROWS; r++)); do
    row=""
    for ((c = 0; c < COLS; c++)); do
      i=$((r * COLS + c))
      if ((!board[i])); then
        row+=" "
      elif ((!old[i])); then
        row+="${YELLOW}o${RESET}"
      else
        row+="${GREEN}o${RESET}"
      fi
    done
    frame+="$row"$'\n'
  done
  tput cup 0 0
  printf "Generation: %-8d Live cells: %-8d Delay: %d ms%s\033[K\n" \
    "$gen" "$alive" "$delay" "$p"
  printf "%s" "$frame"
  printf "[q] Quit  [p] Pause/Resume  [+/-] Speed\033[K"
}

if [[ -n $1 ]]; then init_file "$1"; else init_random; fi
alive=0
for v in "${board[@]}"; do ((alive += v)); done

tput civis
tput clear
stty -echo -icanon

while true; do
  draw
  printf -v t "%d.%03d" $((delay / 1000)) $((delay % 1000))
  if read -rsn1 -t "$t" key; then
    case $key in
      q) break ;;
      p) paused=$((!paused)) ;;
      +) ((delay > 50)) && ((delay -= 50)) ;;    # faster
      -) ((delay += 50)) ;;                      # slower
    esac
  fi
  ((paused)) || step
done
