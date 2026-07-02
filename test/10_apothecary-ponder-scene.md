---
type: test
status: pending
last-updated: 2026-07-02
links: ["[[magic/10_apothecary]]"]
---

# Manual test — Apothecary Ponder scene (`apothecary/brews_pure_daisy`)

GameTest can't verify Ponder scene pacing/legibility (client-only render/overlay/camera
sequencing) — this is a by-eye check. `runClient` needs a display, so this is a human handoff,
not something the agent that built the scene could self-verify.

## Steps

1. `./gradlew runClient` (Ponder scenes are client-side only — `runServer` will not show them)
2. Give yourself an `mam:apothecary` item (creative inventory or `/give`)
3. Hover over it in your inventory — a book icon should appear in the tooltip
4. Click the book icon (or press the Ponder hotkey, default `W`, while hovering) to open the
   Ponder index for the Apothecary
5. Select the "Brewing at the Apothecary" scene

## Expected

- Scene opens on a 5×5 dirt platform with a single Apothecary block, revealed alone first
- Overlay text explains: fill with a fluid, then throw ingredients + catalyst
- A hand-cursor "right-click" prompt appears holding a water bucket, then the Apothecary's basin
  visibly fills with blue water (the real in-game fluid-quad renderer, not a placeholder — confirm
  it rises to the same height/tint as the live block does when actually filled)
- Four white Mystical Petals fall onto the block one at a time and vanish into it; after each one
  lands, the ingredient-orbit renderer should show one more petal icon circling above the fluid
  (again the real renderer — same bobbing/orbiting behavior as the live block, see
  `test/00_apothecary-ingredient-orbit.md`)
- A seed falls onto the block last; shortly after, the fluid visibly drains away, the orbiting
  petals disappear, a success indicator (particles) plays, and a Pure Daisy item pops up above
  the block
- Text pacing is readable — no beat feels rushed or lingers awkwardly; the four petal-throws don't
  feel like a repetitive stutter (if they do, this is a pacing tune, not a functional bug — file a
  follow-up rather than blocking on it)

## Delete this file when

The scene has been visually confirmed at least once and has gone a reasonable stretch without
regressing (e.g. no unrelated renderer/Ponder-plumbing changes touched `ApothecaryBlockEntity`,
`ApothecaryBlockEntityRenderer`, or `MamPonderScenes` since), or an automated screenshot/pixel-diff
test replaces this check.
