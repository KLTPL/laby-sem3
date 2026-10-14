# Arch Linux Installation Notes

## 1. Concepts

### BIOS vs UEFI

- **BIOS** _(Basic Input/Output System)_ – legacy firmware. Initializes hardware, then loads the first 440 bytes of boot code from the MBR of the boot disk, which chains to the bootloader. 16-bit real mode, text-only setup, boots only from MBR disks.
- **UEFI** _(Unified Extensible Firmware Interface)_ – modern firmware. Reads a bootloader (`.efi` file) directly from the **EFI System Partition (ESP)**, a FAT32 partition. Supports GPT and large disks, faster boot, graphical setup, **Secure Boot**, and stores boot entries in NVRAM (managed with `efibootmgr`).
- **Check the mode in the live ISO:** `ls /sys/firmware/efi/efivars` – if the directory exists, the system booted in UEFI mode.

### MBR vs GPT

| Feature              | MBR _(Master Boot Record)_      | GPT _(GUID Partition Table)_                         |
| :------------------- | :------------------------------ | :--------------------------------------------------- |
| Max disk size        | 2 TiB (32-bit sector addresses) | ~9.4 ZB (64-bit sector addresses, 512 B sectors)     |
| Number of partitions | 4 primary (or 3 + 1 extended)   | 128 by default (the table size can be increased)     |
| Redundancy           | None                            | Backup header and table at the end of the disk       |
| Integrity            | None                            | CRC32 checksums                                      |
| Typical firmware     | BIOS                            | UEFI (BIOS can use it via a BIOS boot partition)     |

### Partitioning tools

- **`fdisk`** – interactive, text-based; supports both MBR and GPT. Changes are held in memory until `w` is entered.
- **`cfdisk`** – same capabilities with a curses (menu-driven) interface.
- **`parted`** / **`gdisk`** – alternatives; `gdisk` is GPT-focused.

### Filesystems

- **`ext4`** – default for most Linux distributions. Journaling, stable, good all-round choice (used here for `/`).
- **`ext3`** – predecessor of ext4. Journaling, but no extents and smaller limits.
- **`btrfs`** – copy-on-write, snapshots, subvolumes, built-in checksums and compression.
- **`xfs`** – high-performance journaling filesystem, excellent for large files and parallel I/O; cannot be shrunk.
- **`FAT32`** – required for the ESP, so that UEFI firmware can read it.

---

## 2. Preparing the VM (virt-manager)

- Firmware: **UEFI (OVMF)** – set under _Customize before install → Overview → Firmware_. This is the _virtual_ firmware of the VM; nothing needs to be changed in the BIOS of the host. It cannot be changed after the installation.
  - Choose **`OVMF_CODE_4M.fd`** (plain UEFI, Secure Boot off).
  - Do **not** choose the `.ms.fd` / `.secboot.fd` variants (or the `UEFI` alias, which may map to `secboot`): the Arch ISO is not signed, so the firmware rejects it and returns to the boot menu (the screen only flickers after selecting the DVD-ROM).
  - If it already happened: _firmware menu → Device Manager → Secure Boot Configuration_, untick **Attempt Secure Boot**, save with `F10` and reset.
  - Secure Boot must stay off after the installation too, because the GRUB installed below is not signed.
- Disk bus: **VirtIO** (the disk appears as `/dev/vda`), 20 GB is enough.
- RAM 2 GB, 2 CPUs.
- Take a snapshot of the VM before the installation so you can roll back.

---

## 3. Installation Steps (UEFI + GPT)

### 3.1 Live environment

```sh
loadkeys pl                       # keyboard layout (optional)
ls /sys/firmware/efi/efivars      # verify UEFI mode
ping -c 3 archlinux.org           # verify network
timedatectl                       # check that NTP sync is active
fdisk -l                          # list disks (target: /dev/vda)
```

Optional – SSH from the host to paste commands:

```sh
passwd                            # set the root password in the live ISO
ip a                              # find the IP address
# on the host:
ssh root@<ip>
```

### 3.2 Partitioning

```sh
fdisk /dev/vda
```

Inside `fdisk`:

| Input    | Prompt / action                                                         |
| :------- | :---------------------------------------------------------------------- |
| `g`      | Create a new empty **GPT** table                                        |
| `n`      | New partition (ESP)                                                     |
| `1`      | _Partition number_                                                      |
| Enter    | _First sector_ – accept the default (2048)                              |
| `+1G`    | _Last sector_ – size of 1 GiB (the size goes here, not in First sector) |
| `t`      | Change the partition type (partition 1 is selected automatically)       |
| `uefi`   | _Partition type_ – alias for **EFI System** (`L` lists all types)       |
| `n`      | New partition (root)                                                    |
| Enter ×3 | Accept the defaults: number 2, first sector, last sector = end of disk  |
| `p`      | Print the table and check it                                            |
| `w`      | Write changes and exit (nothing is saved before this)                   |

Expected result: `/dev/vda1` ~1G of type `EFI System`, `/dev/vda2` ~19G of type `Linux filesystem`.

Verify with `fdisk -l /dev/vda` or `lsblk`.

### 3.3 Filesystems and mounting

```sh
mkfs.fat -F 32 /dev/vda1          # ESP
mkfs.ext4 /dev/vda2               # root
mount /dev/vda2 /mnt
mount --mkdir /dev/vda1 /mnt/boot
```

### 3.4 Base system

```sh
pacstrap -K /mnt base linux linux-firmware networkmanager grub efibootmgr sudo nano
genfstab -U /mnt >> /mnt/etc/fstab
arch-chroot /mnt
```

### 3.5 Configuration (inside chroot)

```sh
ln -sf /usr/share/zoneinfo/Europe/Warsaw /etc/localtime
hwclock --systohc

nano /etc/locale.gen              # uncomment en_US.UTF-8 UTF-8
locale-gen
echo "LANG=en_US.UTF-8" > /etc/locale.conf
echo "KEYMAP=pl" > /etc/vconsole.conf   # optional

echo "archvm" > /etc/hostname

passwd                            # root password
useradd -m -G wheel <user>
passwd <user>
EDITOR=nano visudo                # uncomment: %wheel ALL=(ALL:ALL) ALL
```

### 3.6 Bootloader and network

```sh
grub-install --target=x86_64-efi --efi-directory=/boot --bootloader-id=GRUB
grub-mkconfig -o /boot/grub/grub.cfg
systemctl enable NetworkManager
```

### 3.7 Finish

```sh
exit                              # leave chroot
umount -R /mnt
poweroff                          # then detach the ISO in virt-manager
```

Boot the VM again and log in as the new user.

---

## 4. BIOS + MBR Variant (differences only)

- Firmware: **BIOS (SeaBIOS)** in virt-manager.
- In `fdisk` press `o` (new DOS/MBR table) instead of `g`; a single root partition is enough, no ESP.
- Packages: `grub` only, without `efibootmgr`.
- Bootloader: `grub-install --target=i386-pc /dev/vda` (the **disk**, not a partition), then `grub-mkconfig -o /boot/grub/grub.cfg`.

---

## 5. Installation Process Summary

1. **Partitioning** – create the partition table and partitions (`fdisk`).
2. **Filesystems** – format partitions (`mkfs.*`).
3. **Mounting** – mount root at `/mnt` and the ESP at `/mnt/boot`.
4. **Base packages** – `pacstrap` installs the kernel and base system into `/mnt`.
5. **fstab** – `genfstab` records the mounts so they persist across boots.
6. **Bootloader** – GRUB installed to the ESP (UEFI) or the MBR (BIOS).
7. **Network and users** – NetworkManager enabled, root password set, regular user created.

---

## 6. Common Pitfalls

- **No bootloader or forgotten `systemctl enable NetworkManager`** – system does not boot or has no network after the first reboot.
- **ISO still attached** – the VM boots the installer again; detach it first.
- **UEFI vs BIOS mismatch** – `grub-install --target=x86_64-efi` fails on a BIOS-booted live ISO (no `efivars`).
- **Forgotten `genfstab`** – the installed system cannot mount its root filesystem.
