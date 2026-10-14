# Linux Commands Cheat Sheet

## 1. System Information

- **`uname -a`** – Displays comprehensive system information, including the kernel name, kernel version, system architecture, and hostname.
- **`df -h`** – Shows available and used disk space on all mounted filesystems in a human-readable format (e.g., GB, MB).
- **`free -h`** – Displays the total amount of free and used physical (RAM) and swap memory in the system in a human-readable format.
- **`lsb_release -a`** – Prints specific Linux distribution details, including the distributor ID, release version, and codename.

---

## 2. Process Management

- **`ps aux`** – Displays a detailed static snapshot of all currently running processes across all users, showing resource usage (`%CPU`, `%MEM`), process owners, and start times:
  - `a` – processes attached to a terminal (including other users).
  - `u` – user-oriented output format with detailed columns.
  - `x` – background processes not attached to any terminal (daemons).
- **`ps axjf`** – Prints running processes as an ASCII-art hierarchical tree (`f`), exposing parent-child relationships along with job control identifiers (`j` – PPID, PGID, SID).
- **`top`** – Launches a standard, real-time interactive task manager in the terminal, listing active processes and system resource consumption.
- **`htop`** – An enhanced, color-coded, and interactive alternative to `top` featuring visual CPU/RAM meters, mouse support, and easier process management (filtering, tree view, killing processes).

---

## 3. File Permissions

- **`chmod`** _(change mode)_ – Modifies access permissions (Read, Write, Execute) of files and directories for the **User** (`u`), **Group** (`g`), and **Others** (`o`).

### Permission Values

| Symbol            | Octal | File Meaning            | Directory Meaning              |
| :---------------- | :---- | :---------------------- | :----------------------------- |
| **`r`** (read)    | `4`   | Read file contents      | List directory contents (`ls`) |
| **`w`** (write)   | `2`   | Modify and save file    | Create/delete files inside     |
| **`x`** (execute) | `1`   | Run as a program/script | Enter the directory (`cd`)     |
| **`-`** (none)    | `0`   | No permission           | No permission                  |

### Common `chmod` Examples

- **Symbolic mode:**
  - `chmod +x script.sh` – Grants execute permission to everyone.
  - `chmod u-w file.txt` – Removes write permission from the file owner.
- **Octal (numeric) mode:**
  - `chmod 755 script.sh` – Owner gets full access (`7` = `rwx`), Group and Others can read and execute (`5` = `r-x`).
  - `chmod 600 id_rsa` – Owner can read and write (`6` = `rw-`), nobody else has any access (`0` = `---`).

---

## 4. Networking

- **`netstat -tulnp`** – Displays active network connections, listening ports, and the specific programs/processes bound to them (requires `sudo` to view all process names):
  - `-t` – shows TCP ports and connections.
  - `-u` – shows UDP ports and connections.
  - `-l` – displays only **listening** sockets (services waiting for incoming connections).
  - `-n` – shows numerical IP addresses and port numbers instead of resolving hostnames/service names (makes output faster and clearer).
  - `-p` – displays the PID (Process ID) and name of the program holding the socket.

---

## 5. Searching [Files & Text]

### Searching for Files on Disk

- **`find`** – Searches the filesystem in real-time based on various criteria such as name, type, size, or modification time[cite: 1]:
  - `find . -name "file.txt"` – finds `file.txt` in the current directory (`.`) and its subdirectories.
  - `find /home -iname "*.pdf"` – case-insensitive search (`-iname`) for all `.pdf` files inside `/home`.
  - `find . -type d -name "logs"` – searches only for directories (`-type d`, use `-type f` for regular files).
  - `find / -size +100M 2>/dev/null` – finds files larger than 100 MB across the entire system while hiding permission errors.
- **`locate`** – Instantly finds file paths by name using a pre-built system database (much faster than `find`, but may not show newly created files)[cite: 1]:
  - `locate -i report.pdf` – performs a fast, case-insensitive search for `report.pdf`.
  - `sudo updatedb` – manually refreshes the `locate` database to include recently created or moved files.

### Searching Inside Files

- **`grep`** – Searches inside plain-text files or command output for lines matching a specific string or regular expression[cite: 1]:
  - `grep "error" /var/log/syslog` – prints all lines containing `"error"` in the specified file.
  - `grep -in "warning" app.log` – ignores case (`-i`) and displays line numbers (`-n`) for each match.
  - `ps aux | grep firefox` – filters the output of `ps aux` to show only lines matching `firefox`[cite: 1].
- **`grep -r`** – Recursively searches for a text pattern inside all files within a directory and its subdirectories[cite: 1]:
  - `grep -r "192.168.1.1" /etc/` – finds all configuration files inside `/etc/` containing that IP address.
  - `grep -rnI "TODO" .` – searches the current directory recursively (`-r`), showing line numbers (`-n`) and ignoring binary files (`-I`).
  - `grep -rl "failed" /var/log/` – prints only the filenames (`-l`) that contain the word `"failed"`, omitting the matching lines themselves.
