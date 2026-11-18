#!/bin/bash
# Simple system resource monitor using /proc and /sys.

HIST=40                       # number of samples shown in network charts
BARS=(▁ ▂ ▃ ▄ ▅ ▆ ▇ █)
rx_hist=()
tx_hist=()

cleanup() {
  stty sane
  tput cnorm
  tput clear
}
trap cleanup EXIT

# Format bytes/s as B, KB or MB -> result in FMT.
fmt_speed() {
  local b=$1
  if ((b >= 1048576)); then
    FMT="$((b / 1048576)).$((b % 1048576 * 10 / 1048576)) MB/s"
  elif ((b >= 1024)); then
    FMT="$((b / 1024)).$((b % 1024 * 10 / 1024)) KB/s"
  else
    FMT="$b B/s"
  fi
}

# Build one-line bar chart from history array (passed by name) -> CHART.
chart() {
  local -n h=$1
  local max=1 v
  CHART=""
  for v in "${h[@]}"; do ((v > max)) && max=$v; done
  for v in "${h[@]}"; do CHART+=${BARS[v * 7 / max]}; done
}

# Sum received/transmitted bytes of all interfaces except lo -> RX, TX.
read_net() {
  local iface rx f2 f3 f4 f5 f6 f7 f8 tx rest
  RX=0 TX=0
  { read -r; read -r      # skip 2 header lines
    while read -r iface rx f2 f3 f4 f5 f6 f7 f8 tx rest; do
      [[ $iface == lo: ]] && continue
      ((RX += rx, TX += tx))
    done; } </proc/net/dev
}

# Read per-core counters from /proc/stat into CPU_TOTAL[i], CPU_IDLE[i].
read_cpu() {
  local name user nice sys idle iowait irq softirq steal rest i=0
  while read -r name user nice sys idle iowait irq softirq steal rest; do
    [[ $name == cpu[0-9]* ]] || continue
    CPU_TOTAL[i]=$((user + nice + sys + idle + iowait + irq + softirq + steal))
    CPU_IDLE[i]=$((idle + iowait))
    ((i++))
  done </proc/stat
}

show_cpu() {
  local i usage dt di freq
  read_cpu
  for i in "${!CPU_TOTAL[@]}"; do
    dt=$((CPU_TOTAL[i] - PREV_TOTAL[i]))
    di=$((CPU_IDLE[i] - PREV_IDLE[i]))
    usage=$((dt > 0 ? 100 * (dt - di) / dt : 0))
    freq="n/a"
    read -r freq <"/sys/devices/system/cpu/cpu$i/cpufreq/scaling_cur_freq" 2>/dev/null &&
      freq=$((freq / 1000))
    printf "cpu%-3s %3d%%  %5s MHz\033[K\n" "$i" "$usage" "$freq"
  done
  PREV_TOTAL=("${CPU_TOTAL[@]}")
  PREV_IDLE=("${CPU_IDLE[@]}")
}

show_net() {
  local rx_now tx_now elapsed=$((SECONDS - START))
  read_net
  rx_now=$((RX - PREV_RX)) tx_now=$((TX - PREV_TX))
  PREV_RX=$RX PREV_TX=$TX

  rx_hist+=("$rx_now"); tx_hist+=("$tx_now")
  ((${#rx_hist[@]} > HIST)) && rx_hist=("${rx_hist[@]:1}") tx_hist=("${tx_hist[@]:1}")

  ((elapsed < 1)) && elapsed=1
  fmt_speed "$rx_now"; printf "Download: %-12s" "$FMT"
  fmt_speed $(((RX - START_RX) / elapsed)); chart rx_hist
  printf " avg: %-12s %s\033[K\n" "$FMT" "$CHART"
  fmt_speed "$tx_now"; printf "Upload:   %-12s" "$FMT"
  fmt_speed $(((TX - START_TX) / elapsed)); chart tx_hist
  printf " avg: %-12s %s\033[K\n" "$FMT" "$CHART"
}

show_uptime() {
  local up rest
  read -r up rest </proc/uptime
  up=${up%.*}
  printf "Uptime: %dd %02dh %02dm %02ds\033[K\n" \
    $((up / 86400)) $((up % 86400 / 3600)) $((up % 3600 / 60)) $((up % 60))
}

show_battery() {
  local line cap="n/a"
  if [[ -r /sys/class/power_supply/BAT0/uevent ]]; then
    while read -r line; do
      [[ $line == POWER_SUPPLY_CAPACITY=* ]] && cap="${line#*=}%"
    done </sys/class/power_supply/BAT0/uevent
  fi
  printf "Battery: %s\033[K\n" "$cap"
}

show_load() {
  local l1 l5 l15 rest
  read -r l1 l5 l15 rest </proc/loadavg
  printf "Load average: %s %s %s (1, 5, 15 min)\033[K\n" "$l1" "$l5" "$l15"
}

# MemTotal, MemFree, MemAvailable are the first 3 lines of /proc/meminfo (kB).
show_mem() {
  local name total free avail unit
  { read -r name total unit; read -r name free unit; read -r name avail unit; } </proc/meminfo
  printf "Memory: used %d MB / %d MB (free %d MB, available %d MB)\033[K\n" \
    $(((total - avail) / 1024)) $((total / 1024)) $((free / 1024)) $((avail / 1024))
}

# Initial samples so the first deltas make sense.
read_net; PREV_RX=$RX PREV_TX=$TX START_RX=$RX START_TX=$TX START=$SECONDS
read_cpu; PREV_TOTAL=("${CPU_TOTAL[@]}") PREV_IDLE=("${CPU_IDLE[@]}")

tput civis
tput clear
stty -echo -icanon

while true; do
  read -rsn1 -t 1 key && [[ $key == q ]] && break
  tput cup 0 0
  show_net
  echo -e "\033[K"
  show_cpu
  echo -e "\033[K"
  show_uptime
  show_battery
  show_load
  show_mem
  printf "\n[q] quit\033[K"
  tput ed
done
