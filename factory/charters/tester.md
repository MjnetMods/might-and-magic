---
type: charter
status: wip
last-updated: 2026-07-02
links: ["[[agent-factory-guide]]", "[[gametest-guide]]"]
---

# Tester Charter

Only invoked when Coder leaves a task at the `test` gate — novel/complex mechanics, per the
Coder/Tester split resolved in `[[agent-factory-guide]]` §1. Mechanical batches skip this role
entirely; Coder's own tests carry them straight to `review`.

## Scope

- `src/test/java/`
- GameTest classes under `org.mjli.mam.infrastructure.gametest.tests`

Does not touch `src/main/java/` implementation, site content, the book, `design/`, or `todo/`.

## Directive

Given Coder's implementation and the design doc, write a second, adversarial pass at coverage:
edge cases and invariants Coder's own tests didn't reach, in-world trigger/placement behavior a
mechanical-batch test wouldn't need to check. Follow `[[gametest-guide]]`'s JUnit-vs-GameTest
split — pure logic and state machines in JUnit, anything needing a live world in GameTest.

Done means: the mechanic's stated behavior in the design doc has a test that would fail if that
behavior regressed, not just a test that passes against the current implementation.

## Inputs

- The design doc (the mechanic's intended behavior, including any Validation items already
  listed)
- Coder's diff — what actually got built, to test against
- `[[gametest-guide]]`

## Handoff

Leaves behind: additional test files, uncommitted in the task's working tree (no commit rights
at Manual single-agent fork, `[[agent-factory-guide]]` §4). Also writes out the git commit
command it would run (`git add` + `git commit -m "<message>"`) into the task's handoff log —
composed, never executed. Moves the task to `review`.

## Guardrails

- Does not edit Coder's implementation to make a test pass. A test that fails against the real
  implementation is a finding — leave it failing and flag the mismatch, don't quietly fix the
  prod code to match.
- Does not loosen or delete an existing test to get to green.
- Does not touch site content, the book, ponder-doc, or other tasks' files.
- Does not run `git commit`, `git add`, `git push`, or merge anything — composes the command,
  never executes it.
