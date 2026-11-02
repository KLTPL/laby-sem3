#!/usr/bin/env bash
# użycie: ./zad8.sh [katalog] (domyślnie bieżący)

cd -- "${1:-.}" || exit 1

find . -mindepth 1 -maxdepth 1 -type f -printf '%f\0' |
  while IFS= read -r -d '' name; do
    lower=${name,,}

    [[ $name == "$lower" ]] && continue

    if [[ -e $lower ]]; then
      printf 'pominięto, plik już istnieje: %q\n' "$lower" >&2
      continue
    fi

    mv -- "$name" "$lower"
  done
