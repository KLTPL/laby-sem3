# zad5

A mini container in C: `clone()` with new PID, UTS and mount namespaces, `chroot` into a BusyBox rootfs and `/bin/sh` inside.

## Created with

`mini_container.c` is the example program from the assignment (only reformatted). Needs `gcc` and `wget`.

```bash
# 1. rootfs directory
mkdir -p mini_rootfs/{bin,proc,sys,dev}

# 2. statically linked BusyBox (no libc needed inside the chroot)
wget https://busybox.net/downloads/binaries/1.35.0-x86_64-linux-musl/busybox -O mini_rootfs/bin/busybox
chmod +x mini_rootfs/bin/busybox

# 3. applets used as commands (relative symlinks, they resolve inside the chroot)
ln -s busybox mini_rootfs/bin/sh
ln -s busybox mini_rootfs/bin/ps
ln -s busybox mini_rootfs/bin/ls

# 4. build
gcc -o mini_container mini_container.c
```

`mini_container` and `mini_rootfs/` are in `.gitignore`, recreate them with the commands above.

## Launching

Root is required (`clone` with `CLONE_NEW*` flags, `chroot`, `mount`).

```bash
sudo ./mini_container ./mini_rootfs # exit the shell with `exit`
```

## How it works

| Step                                    | Purpose                                                               |
| :-------------------------------------- | :-------------------------------------------------------------------- |
| `clone(CLONE_NEWPID)`                   | The child sees itself as PID 1                                        |
| `clone(CLONE_NEWUTS)`                   | Own hostname, `sethostname()` does not change the host                |
| `clone(CLONE_NEWNS)`                    | Own mount table, mounts do not leak to the host                       |
| `mount(NULL, "/", MS_REC \| MS_PRIVATE)` | Stops mount propagation to the host's namespace                       |
| `chroot(rootfs)` + `chdir("/")`         | The rootfs directory becomes `/`                                      |
| `mount("proc", "/proc", "proc")`        | `/proc` matching the new PID namespace, so `ps` shows only the container |
| `execvp("/bin/sh")`                     | Replaces the child with the shell                                     |

The parent only calls `waitpid()` on the child.

## Notes

- The stack for `clone()` is passed as `child_stack + STACK_SIZE`, because the stack grows downward on x86.
- BusyBox must be static: there are no host libraries inside the chroot.
- `chroot` is not a security boundary (a root process can escape it), real runtimes use `pivot_root`.

## TODO

- [ ] Network isolation: `CLONE_NEWNET` + a `veth` pair between the host and the container.
- [ ] Resource limits: put the container PID in a cgroup v2 group and set `memory.max = 50M`, then show the OOM kill.
