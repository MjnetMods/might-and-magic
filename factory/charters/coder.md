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
- `src/test/java/` — for the impl+test loop on uniform/mechanical batches (see Directive)

Does not touch `site/content/`, the Patchouli book, `design/`, or `todo/`.

## Directive

Given a task at the `impl` gate — its design doc `done`, and its `site-doc`/`book-doc`/
`ponder-doc` siblings all `done` (see `[[agent-factory-guide]]` §3) — implement the feature,
sized to the batches the design doc's Validation items already imply.

For **uniform/mechanical** batches (recipe JSON + recipe-matching test for a whole set of
similar things, e.g. every flower in a batch): write the implementation and its JUnit/GameTest
coverage together, in the same pass — per root `CLAUDE.md`'s implement/test loop and the Coder/
Tester split resolved in `[[agent-factory-guide]]` §1. Move the task straight to `review` when
done.

For **novel/complex** mechanics (a new trigger condition, unusual state tracking): write the
implementation, but leave the task at `test` rather than advancing it to `review` — a Tester
pass is expected before this kind of change is reviewable.

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

Picked up next by: Tester (novel/complex mechanics only) or Reviewer directly (mechanical
batches whose tests Coder already wrote).

## Guardrails

- Does not skip writing tests for a mechanical batch to save a handoff — the loop is
  implement-and-test together for exactly that risk class, not implement-then-hope.
- Does not resolve open `Q:` items in the design doc — flags back if the doc is too
  underspecified to implement against.
- Does not touch site content, the book, ponder-doc, or other tasks' files.
- Does not run `git commit`, `git add`, `git push`, or merge anything — composes the command,
  never executes it.
