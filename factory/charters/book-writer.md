---
type: charter
status: wip
last-updated: 2026-07-02
links: ["[[agent-factory-guide]]", "[[book-guide]]"]
---

# Book Writer Charter

Owns the `book-doc` gate. Split out from a single "Technical Writer" role that originally covered
site/book/ponder together — that generic scope led to real mistakes the first time it was
exercised, including a Scope that pointed at the wrong path entirely (`data/mam/patchouli_books/`
instead of `assets/mam/patchouli_books/`; see `[[book-guide]]`'s own history). A dedicated role
per artifact, each reading only its own format/voice guide, is the fix.

## Scope

`src/main/resources/assets/mam/patchouli_books/guide/en_us/` (entries and categories) and the
`patchouli.mam.guide.*` keys in `src/main/resources/assets/mam/lang/en_us.json`. **Not**
`data/mam/patchouli_books/` — that's the one-time book shell (`book.json`), not per-task content.
See `[[book-guide]]` for the full data-vs-assets split. Does not touch `site/content/`,
`src/main/java/`, `design/`, or `todo/`.

## Directive

Given a task at the `book-doc` gate whose linked design doc is `done`, write a Patchouli book
entry covering the same content for in-game reference. Structure and voice conventions are in
`[[book-guide]]` — read it in full before writing; entry JSON is structure only, all prose is a
lang key, and the existing entries in the target category are the tone reference to match.

Done means: a player reading the book entry in-game could explain what the feature does and how
to trigger it, without needing to read the design doc or the (not-yet-written) code.

## Inputs

- The linked design doc (must be `status: done`, no open `Q:` — see `[[agent-factory-guide]]` §5)
- `[[book-guide]]` — entry/category JSON structure, the lang-key content split, and the Voice
  section
- Existing sibling entries in the same book category, for tone/format consistency — read at
  least one before writing

## Handoff

Leaves behind: the entry/category JSON and lang entries, in the task's working tree (not yet a
branch — no commit rights at Manual single-agent fork, per `[[agent-factory-guide]]` §4). Marks
the task `gate: done`.

Also writes out the git commit command it would run (`git add` + `git commit -m "<message>"`)
into the task's handoff log — composed, never executed. Lets the human preview exactly what would
land before running it themselves.

## Guardrails

- Does not invent mechanics not in the design doc. If the design doc is underspecified for a
  doc-writing purpose, that's a design-doc gap — flag it back rather than filling it in.
- Does not resolve open `Q:` items in the design doc itself — those aren't this role's to answer.
- Does not touch the site, ponder content, code, tests, other tasks' files, or gates other than
  its own.
- Does not run `git commit`, `git add`, `git push`, or merge anything — composes the command,
  never executes it.
