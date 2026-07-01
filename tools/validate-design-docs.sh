#!/usr/bin/env bash
# Validates markdown docs under a directory against ref/design-doc-guide.md.
# Usage: validate-design-docs.sh <directory>
set -euo pipefail

root="${1:?usage: validate-design-docs.sh <directory>}"
root="${root%/}"
# links may point outside root to a sibling folder (e.g. design/ -> ref/),
# so resolve them from one level up rather than root itself
search_root="$(dirname "$root")"
allowed_status=("todo" "wip" "done" "blocked")
errors=0

is_allowed_status() {
  local s="$1"
  for a in "${allowed_status[@]}"; do
    [[ "$s" == "$a" ]] && return 0
  done
  return 1
}

while IFS= read -r -d '' file; do
  rel="${file#"$root"/}"
  base="$(basename "$file")"

  # 1. filename prefix must be pure digits followed by an underscore
  if [[ ! "$base" =~ ^[0-9]+_ ]]; then
    echo "ERROR: $rel — filename prefix is not purely numeric"
    errors=$((errors + 1))
  fi

  # strip fenced code blocks so example content (TOML, markdown, etc.) inside
  # them isn't mistaken for real headings or wikilinks
  stripped=$(awk '/^```/{f=!f; next} !f' "$file")

  # 2. exactly one top-level heading
  h1_count=$(printf '%s\n' "$stripped" | grep -c '^# ' || true)
  if [[ "$h1_count" -ne 1 ]]; then
    echo "ERROR: $rel — expected exactly one top-level heading, found $h1_count"
    errors=$((errors + 1))
  fi

  # 3. front matter present, with a status in the allowed set
  if [[ "$(sed -n '1p' "$file")" == "---" ]]; then
    fm_end=$(awk '/^---$/{c++; if (c==2) {print NR; exit}}' "$file")
    if [[ -z "$fm_end" ]]; then
      echo "ERROR: $rel — front matter opened but never closed"
      errors=$((errors + 1))
    else
      status_line=$(sed -n "1,${fm_end}p" "$file" | grep -m1 '^status:' || true)
      status_val=$(echo "$status_line" | sed -E 's/^status:[[:space:]]*//; s/[[:space:]]*$//' | tr -d "\"'")
      if [[ -z "$status_val" ]]; then
        echo "ERROR: $rel — front matter missing status field"
        errors=$((errors + 1))
      elif ! is_allowed_status "$status_val"; then
        echo "ERROR: $rel — status '$status_val' not in todo/wip/done/blocked"
        errors=$((errors + 1))
      fi
    fi
  else
    echo "ERROR: $rel — missing front matter"
    errors=$((errors + 1))
  fi

  # 4. [[wikilink]] targets must resolve to a real file — either the exact
  # relative path (folder-qualified, e.g. [[magic/00_energy]] or
  # [[ref/gametest-guide]]) or, failing that, a unique basename anywhere
  # under the project root
  while IFS= read -r link; do
    name="${link//[\[\]]/}"
    if [[ -f "$root/${name}.md" ]] || [[ -f "$search_root/${name}.md" ]]; then
      continue
    fi
    if ! find "$search_root" -iname "$(basename "$name").md" -print -quit 2>/dev/null | grep -q .; then
      echo "ERROR: $rel — [[${name}]] does not resolve to a file"
      errors=$((errors + 1))
    fi
  done < <(printf '%s\n' "$stripped" | grep -oE '\[\[[^]]+\]\]' || true)

  # informational: how many open questions remain — each resolved question is
  # a **Q:**/**A:** pair, so open count is just Q's minus A's
  q_count=$(grep -c '\*\*Q:\*\*' "$file" || true)
  a_count=$(grep -c '\*\*A:\*\*' "$file" || true)
  open_count=$((q_count - a_count))
  if [[ "$open_count" -gt 0 ]]; then
    echo "INFO: $rel — $open_count open question(s)"
  fi

done < <(find "$root" -name '*.md' -print0)

if [[ "$errors" -gt 0 ]]; then
  echo "$errors error(s) found."
  exit 1
fi

echo "All design docs pass format checks."
