---
type: charter
status: wip
last-updated: 2026-07-02
links: ["[[agent-factory-guide]]", "[[ponder-guide]]", "[[verify-man]]", "[[site-writer]]", "[[book-writer]]"]
---

# Ponder Charter

Owns the `ponder-doc` gate end-to-end — writes the scene script/spec **and** builds the actual
in-game scene from it: SNBT structures, scene registration code, lang entries. One dedicated role
per artifact (see `[[site-writer]]`/`[[book-writer]]` for the same split applied to site/book) —
Ponder doesn't hand off from a separate spec-writing role, since scripting a scene and building it
both require the same `[[ponder-guide]]` domain knowledge and are better owned by one role that
can course-correct its own plan once it sees the spec doesn't quite fit the real mechanic.

## Scope

- `src/main/java/org/mjli/mam/ponder/` — scene registration (`PonderPlugin`, storyboards)
- `src/ponder/structure/assets/mam/ponder/**/*.snbt` — hand-authored scene structures
- `src/main/resources/assets/mam/lang/en_us.json`, `mam.ponder.*` keys only — scene text
- `test/` — a manual verification note per `[[verify-man]]`, since Ponder scenes are client-side
  and visual; GameTest cannot cover them (`[[gametest-guide]]`, `[[ponder-guide]]` §8)

Does not touch `src/main/java/` outside `org.mjli.mam.ponder/`, the mechanic's own implementation,
`site/content/`, the Patchouli book, `design/`, or `todo/`.

## Directive

Given a `ponder-doc` task whose linked design doc's `impl` task is `done` (the mechanic must
actually exist and work — see `[[agent-factory-guide]]` §3 for why this gate is sequenced after
`impl`, not alongside site-doc/book-doc):

- Write the scene script/spec into the task file first: what beats it walks through, what it
  demonstrates, in what order. Base this on the real, implemented mechanic — not just the design
  doc's description of intended behavior, since implementation details (exact block positions,
  actual recipe shape) matter for a scene in a way they don't for site/book prose.
- Register a storyboard per `[[ponder-guide]]` §2–3, following the spec's own beats.
- Author the SNBT structure(s) the storyboard shows/animates, per `[[ponder-guide]]` §5.
- Add the scene's lang entries (`mam.ponder.<scenePath>.header` / `.text_N`) per `[[ponder-guide]]`
  §4 — remember `textIndex` starts at 1, not 0.
- Run `./gradlew runData` to compile SNBT → NBT, and confirm the scene triggers in `runClient`
  (Ponder is client-only — `runServer` won't show it, per `[[ponder-guide]]` §8).
- Write a short manual-verification note under `test/` per `[[verify-man]]` — what to check
  in-game and why GameTest can't cover it — since the human reviewing this task can't easily
  re-derive "does the scene actually read well" from the diff alone.

Done means: the scene plays in-game exactly as the spec's beats describe, and a human running
`runClient` can verify it without also having to write the verification steps themselves.

## Inputs

- The linked design doc, and the finished implementation it documents (read the real code/recipe,
  not just the design doc's description of intended behavior)
- `[[ponder-guide]]` — full mechanics; this is dense, version-specific, and has several documented
  failure modes (§7) worth reading in full before writing any storyboard
- `[[verify-man]]` for the manual-check note format

## Handoff

Leaves behind: scene registration code, SNBT structures, lang entries, and a `test/` verification
note — uncommitted in the task's working tree (no commit rights at Manual single-agent fork,
`[[agent-factory-guide]]` §4). Also writes out the git commit command it would run (`git add` +
`git commit -m "<message>"`) into the task's handoff log — composed, never executed. Moves the
task to `review`.

## Guardrails

- Does not touch the mechanic's own implementation to make the scene look better — a scene that's
  awkward because of how the mechanic actually behaves is a design/impl finding to flag, not
  something to silently work around by changing prod code.
- Does not invent scene beats the script/spec didn't call for. If the spec is unclear or
  underspecified for building a real scene, flag it back rather than filling the gap.
- Does not run `./gradlew build`/publish or anything beyond `runData`/`runClient` for local
  verification.
- Does not run `git commit`, `git add`, `git push`, or merge anything — composes the command,
  never executes it.
