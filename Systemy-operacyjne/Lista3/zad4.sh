#!/bin/bash
# Show a random cat picture as ASCII/ANSI art and a random Chuck Norris joke.
# Requires: curl, jq, catimg

set -euo pipefail

IMG=$(mktemp --suffix=.jpg)
trap 'rm -f "$IMG"' EXIT

# 1. Cat API returns a JSON array: [{"id": ..., "url": ..., ...}]
cat_url=$(curl -fsS https://api.thecatapi.com/v1/images/search | jq -r '.[0].url')
curl -fsS -o "$IMG" "$cat_url"
catimg -w "$(tput cols)" "$IMG"

# 2. Chuck Norris API returns a JSON object: {"value": "<joke>", ...}
joke=$(curl -fsS https://api.chucknorris.io/jokes/random | jq -r '.value')
echo
echo "$joke"
