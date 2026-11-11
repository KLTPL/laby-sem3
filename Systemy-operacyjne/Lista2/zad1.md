# Zad1

## dd + hexdump

```bash
sudo dd if=/dev/nvme0n1 bs=512 count=1 | hexdump -C
```

## pamięć, procesor, partycje w /proc

```bash
cat /proc/meminfo
cat /proc/cpuinfo
cat /proc/partitions
```

## Opis katalogów

▎ /dev – pliki urządzeń: przez nie czytam i piszę dane do sprzętu.
▎ /proc – informacje o procesach (i historycznie trochę o sprzęcie), generowane przez kernel przy odczycie.
▎ /sys – opis sprzętu, który zna kernel: co jest podłączone gdzie, jaki sterownik to obsługuje i jakie ma parametry. Na podstawie /sys udev tworzy
▎ pliki w /dev.
