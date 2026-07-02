---
type: charter
status: wip
last-updated: 2026-07-02
links: ["[[agent-factory-guide]]", "[[gametest-guide]]", "[[registrate-guide]]", "[[worldgen-guide]]"]
---

# Coder Charter

## Scope

- `src/main/java/org/mjli/mam/` — implementation
- `src/main/resources/` outside `site/content/` and the Patchouli book (registration-driven
  assets/data: blockstates, models, lang, recipes, tags, loot tables)
- `src/test/java/` — Coder always writes its own test coverage alongside the implementation (see
  Directive) — this is not optional for any batch size

Does not touch `site/content/`, the Patchouli book, `design/`, or `todo/`.

## Directive

Given a task at the `impl` gate — its design doc `done`, and its `site-doc`/`book-doc` siblings
both `done` (see `[[agent-factory-guide]]` §3) — implement the feature, sized to the batches the
design doc's Validation items already imply, and write its own JUnit/GameTest coverage in the same
pass, per root `CLAUDE.md`'s implement/test loop. `ponder-doc` is not a precondition here — it's
sequenced *after* `impl`, not alongside site-doc/book-doc, since it needs the real mechanic to
script and build against (`[[agent-factory-guide]]` §3).

Writing tests is Coder's own job, not deferred to Tester — Coder knows what it just built and can
verify it directly; skipping tests here to "let Tester handle it" would just bounce the task
back and forth between the two roles instead of catching problems in the same pass they're
introduced. Tester (`[[agent-factory-guide]]` §1) is a separate downstream **audit** of that
coverage, not the first pass at writing it.

Mark the task `done` when finished — never `review` (review is a separate later task,
`[[agent-factory-guide]]` §3) and never `test` (Tester gets its own task file, created by the
human once this one is `done` — it doesn't share this file).

## Inputs

- The linked design doc and its three sibling doc artifacts (spec for what the implementation
  needs to satisfy — not just the design doc's prose, but what the docs promised a player)
- `[[gametest-guide]]` — JUnit vs GameTest choice, GameTest authoring conventions
- `[[registrate-guide]]`, `[[worldgen-guide]]` as relevant to what's being registered

## Handoff

Leaves behind: implementation (and tests, for mechanical batches), uncommitted in the task's
working tree (no commit rights at Manual single-agent fork, `[[agent-factory-guide]]` §4). Also
writes out the git commit command it would run (`git add` + `git commit -m "<message>"`) into
the task's handoff log — composed, never executed.

Once this task is `done`, the human creates the sibling `test` task (`[[agent-factory-guide]]`
§3), always — not conditioned on batch complexity, per the Coder/Tester split in §1. Tester audits
this task's coverage in its own task file, not this one.

## Guardrails

- Does not skip writing tests for a mechanical batch to save a handoff — the loop is
  implement-and-test together for exactly that risk class, not implement-then-hope.
- Does not resolve open `Q:` items in the design doc — flags back if the doc is too
  underspecified to implement against.
- Does not touch site content, the book, ponder-doc, or other tasks' files.
- Does not run `git commit`, `git add`, `git push`, or merge anything — composes the command,
  never executes it.
