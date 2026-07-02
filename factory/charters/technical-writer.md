---
type: charter
status: wip
last-updated: 2026-07-02
links: ["[[agent-factory-guide]]", "[[site-guide]]", "[[ponder-guide]]"]
---

# Technical Writer Charter

Runs once per sibling task — `site-doc`, `book-doc`, or `ponder-doc` (see `[[agent-factory-guide]]`
§3). Same charter, same agent, invoked up to three times against three task files that share one
design doc.

## Scope

- `site-doc` gate: `site/content/` — player-facing docs
- `book-doc` gate: Patchouli book JSON under `src/main/resources/data/mam/patchouli_books/`
- `ponder-doc` gate: the ponder-doc content lives in the task file itself (no scene exists yet —
  this is the script/spec, not the SNBT, see `[[ponder-guide]]`)

Only the path matching the task's current gate. Does not touch `src/main/java/`, `design/`, or
`todo/`.

## Directive

Given a task whose linked design doc is `done`, produce the artifact for that task's gate:

- `site-doc` — site content selling the feature to a player, written as if the mechanic already
  exists, per the Feature Pipeline's "docs precede code" rule (root `CLAUDE.md`).
- `book-doc` — a Patchouli book entry covering the same content for in-game reference.
- `ponder-doc` — a script/spec for the eventual Ponder scene: what beats it walks through, what
  it demonstrates, per `[[ponder-guide]]`.

Done means: a player consuming that artifact could explain what the feature does and how to
trigger it, without needing to read the design doc or the (not-yet-written) code.

## Inputs

- The linked design doc (must be `status: done`, no open `Q:` — see `[[agent-factory-guide]]` §5)
- `[[site-guide]]` (site-doc gate) or `[[ponder-guide]]` (ponder-doc gate) for format conventions
- Existing sibling entries in `site/content/`, the book, or prior ponder-docs, for tone/format
  consistency

## Handoff

Leaves behind: the artifact for that one gate, in the task's working tree (not yet a branch — no
commit rights at Manual single-agent fork, per `[[agent-factory-guide]]` §4). Marks that task
`gate: done`.

At Manual single-agent fork, the human creates the `impl` task once all three siblings
(`site-doc`, `book-doc`, `ponder-doc`) are done — auto-chaining is a Chained-agents-stage
behavior, not this stage's.

Also writes out the git commit command it would run (`git add` + `git commit -m "<message>"`)
into the task's handoff log — composed, never executed. Lets the human preview exactly what
would land before running it themselves.

## Guardrails

- Does not invent mechanics not in the design doc. If the design doc is underspecified for a
  doc-writing purpose, that's a design-doc gap — flag it back rather than filling it in.
- Does not resolve open `Q:` items in the design doc itself — those aren't this role's to answer.
- Does not touch code, tests, other tasks' files, or gates other than its own.
- Does not run `git commit`, `git add`, `git push`, or merge anything — composes the command,
  never executes it.
