# zad4

## Created with

```bash
sudo apt install build-essential git gdb qemu-system-x86 qemu-system-misc gcc-riscv64-linux-gnu gdb-multiarch
git clone https://github.com/mit-pdos/xv6-public.git
cd xv6-public
wget https://cs.pwr.edu.pl/zawada/so/xv6.patch
patch -p1 -i xv6.patch
```

## Launching

```bash
make qemu-nox # ctrl+a x to exit
```

## Debugging

### Terminal 1

```bash
make qemu-nox-gdb

```

### Terminal 2

```bash
echo "set auto-load safe-path /" >> ~/.gdbinit # run ONLY ONCE
gdb ./kernel
(gdb) break *0x7c00
(gdb) c
```


## xv6-64 (x86-64)

### Created with

Same packages as above (`qemu-system-x86`, `gdb`). No `xv6.patch` needed, it builds with a recent gcc as is.

```bash
git clone https://gitlab.com/xv6-64/xv6-64.git
cd xv6-64
```

### Launching

```bash
make qemu-nox # ctrl+a x to exit
```

### Debugging

Terminal 1:

```bash
make qemu-nox-gdb
```

Terminal 2 (`auto-load safe-path` is already set from xv6-public):

```bash
gdb ./kernel
(gdb) break *0x7c00
(gdb) c
```

Differences from the x86-32 version:

- gdb prints `Architecture 'i8086' not recognized` and `Error while running hook_stop: Value can't be converted to integer`. This is harmless: `.gdbinit` was written for 32-bit (`set architecture i8086`, `$eip`), while QEMU reports `i386:x86-64`. The breakpoint at `0x7c00` is still hit.
- Use `x/3i $pc` and `info registers rip cs` to inspect the state manually.

## xv6-riscv (RISC-V)

### Created with

Needs `qemu-system-misc` (QEMU) and `gdb-multiarch` (plain Ubuntu `gdb` is x86 only), both in the install line above.

```bash
git clone https://github.com/mit-pdos/xv6-riscv.git
cd xv6-riscv
```

If the build fails with `invalid argument ... to '-ffile-prefix-map'`, the path to the repo contains a space. Move the repo to a path without spaces.

### Launching

There is no `qemu-nox` target. `make qemu` already runs without a GUI (console over UART in the terminal).

```bash
make qemu # ctrl+a x to exit
```

### Debugging

Terminal 1:

```bash
make qemu-gdb
```

Terminal 2 (the generated `.gdbinit` connects to port 26000 and loads symbols):

```bash
gdb-multiarch kernel/kernel
(gdb) b _entry
(gdb) c
```

Differences from x86:

- There is no BIOS and no `0x7c00` boot sector. QEMU is started with `-bios none` and jumps straight to the kernel at `0x80000000` (`_entry`).
- Registers are `pc`, `sp`, etc. instead of `eip`/`rip`: `x/3i $pc`, `info registers pc`.
