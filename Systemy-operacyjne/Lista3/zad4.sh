#!/bin/bash
# Show a random cat picture as ASCII/ANSI art and a random Chuck Norris joke.
# Requires: curl, jq, catimg

set -euo pipefail # -e: end the script when something fails, -u throw an error when a variable is unidentified, -o throw error if one of a or b in a | b fail.

IMG=$(mktemp --suffix=.jpg) 
trap 'rm -f "$IMG"' EXIT

cat_url=$(curl -fsS https://api.thecatapi.com/v1/images/search | jq -r '.[0].url') # -f: fail when returns a network error, -sS silent but show
curl -fsS -o "$IMG" "$cat_url"
catimg -w "$(tput cols)" "$IMG" # tput cols - terminal width

joke=$(curl -fsS https://api.chucknorris.io/jokes/random | jq -r '.value')
echo
echo "$joke"
