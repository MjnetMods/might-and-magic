---
type: task
gate: done
last-updated: 2026-07-02
links: ["[[magic/10_apothecary]]"]
---

# T2 Apothecary — Ponder Doc

Owned end-to-end by [[ponder]] (script + scene, see that charter for why it's one role rather than
a spec-writer/builder handoff). Blocked on both `03` (impl) and `04` (test) being `done` — this
task scripts and builds against the real, implemented *and verified* T2 recipe, not the design
doc's description of it. Waiting on `04` too, not just `03`, matters here specifically: if Tester's
audit turns up a real gap and Coder has to adjust behavior to close it, a scene already built
against the pre-fix behavior would need rework.

Scene: the T1 → T2 upgrade path (gathering Infused Living Rock, placing the goblet, dropping the
T1 Apothecary in as the secondary ingredient), then a quick craft using the extra slots to make
the point of the tier concrete. See [[magic/10_apothecary]] for the mechanic and [[ponder-guide]]
for scene/SNBT format.

## Handoffs

- 2026-07-02 — task created (human), gate: ponder-doc
- 2026-07-02 — reassigned from Technical Writer (script-only) to the new dedicated [[ponder]]
  charter (script + scene build), part of splitting the original generic doc-writing role into
  one charter per artifact — see [[agent-factory-guide]] §1.
- 2026-07-02 — [[ponder]] built the scene end-to-end, gate: done. Scope call made and documented
  in the Scene script/spec below: does *not* show a "quick craft using the extra slots" demo, since
  that requires the T1 in-world ingredient-throwing mechanic, which has no Ponder scene of its own
  yet (T1's `mam:apothecary` had zero storyboards registered before this task — this is its
  first). Simulating that mechanic here, ahead of a dedicated T1 scene, risked getting the fluid/
  orbit staging wrong without the reference of an already-built T1 scene to match pacing against.
  Flagging this back rather than inventing it, per the charter's guardrail against filling
  unclear/underspecified scope gaps silently. Recommend a future task: "T1 Apothecary — ingredient/
  catalyst mechanic Ponder scene", which this T2 scene could then reference/follow directly.

  Files changed:
  - `src/main/java/org/mjli/mam/ponder/MamPonderScenes.java` — added `apothecaryInfusedUpgrade()`
    storyboard + registration against `VerdantMana.APOTHECARY` (T1's first Ponder entry)
  - `src/ponder/structure/assets/mam/ponder/apothecary/infused_upgrade.snbt` — new structure:
    5×5 dirt base plate, T1 Apothecary + 6 Infused Living Rock in the recipe's goblet shape at y=1
  - `src/main/resources/assets/mam/lang/en_us.json` — added
    `mam.ponder.apothecary.infused_upgrade.{header,text_1,text_2,text_3}`
  - `test/10_apothecary-t2-ponder-scene.md` — manual verification note (status: pending — not
    visually confirmed this session, no display/runClient capability available; see note below)
  - This task file — scene script/spec (below) and this handoff entry

  Verified: `./gradlew runData` — SNBT compiled cleanly to
  `src/generated/resources/assets/mam/ponder/apothecary/infused_upgrade.nbt`, no errors.
  `./gradlew compileJava` — clean (already compiled as part of the `runData` run, since datagen
  needs the compiled classes).

  **Not verified — flagging explicitly per the charter's own Scope section:** did not run
  `./gradlew runClient` and cannot confirm the scene visually plays/reads correctly in-game. No
  display/long-running GUI capability available in this environment. `test/
  10_apothecary-t2-ponder-scene.md` is written so a human with a client can confirm it; `status:
  pending` until someone does.

  **Composed commit (not executed — no `git add`/`git commit` run):**
  ```
  git add src/main/java/org/mjli/mam/ponder/MamPonderScenes.java \
    src/ponder/structure/assets/mam/ponder/apothecary/infused_upgrade.snbt \
    src/main/resources/assets/mam/lang/en_us.json \
    test/10_apothecary-t2-ponder-scene.md \
    factory/tasks/05_apothecary-t2-ponder-doc.md

  git commit -m "feat: apothecary T1->T2 upgrade ponder scene

  Storyboard on mam:apothecary showing the Infused Apothecary upgrade recipe
  (6x Infused Living Rock + T1 Apothecary -> Infused Apothecary) staged as an
  in-world goblet-shape reveal rather than a simulated crafting-table GUI.
  Scoped out the in-world ingredient/catalyst mechanic demo (T1's own scene,
  not yet built) - see factory/tasks/05_apothecary-t2-ponder-doc.md handoff log."
  ```

- 2026-07-02 — **reverted by human.** On reflection, this task's own brief was wrong at the root:
  a static crafting-table recipe (already visible via the vanilla recipe book) isn't Ponder
  material — Ponder is for non-obvious, dynamic in-world mechanics, not for restating a recipe
  grid the player can already see. The scene itself was well-executed against the brief it was
  given (see the build log above and the scope note it already correctly flagged), but the brief
  was the mistake, not the execution. Reverted: `MamPonderScenes.java`'s
  `apothecaryInfusedUpgrade()` method + registration, the 4 `mam.ponder.apothecary.infused_upgrade.*`
  lang keys, `src/ponder/structure/assets/mam/ponder/apothecary/infused_upgrade.snbt`, and
  `test/10_apothecary-t2-ponder-scene.md` — all removed, no trace left in the working tree.
  `[[ponder]]`'s charter was updated with an explicit "no scene needed, motivate why, move on" exit
  path so a future task doesn't get force-fitted into an unwarranted scene the same way. The actual
  useful scene — T1's in-world fill/throw/catalyst mechanic — is tracked separately as
  `07_apothecary-t1-mechanic-ponder-doc.md`. This task (`05`) stays `gate: done` as an honest
  historical record (it did complete its stated brief; the brief itself was later judged wrong),
  not reopened.

## Scene script/spec

Storyboard key: `apothecary/infused_upgrade` (structure: `apothecary/infused_upgrade.snbt`).
`scene.title(...)` id: `apothecary.infused_upgrade`. Registered against `VerdantMana.APOTHECARY`
(the T1 block) — the item players actually hold when they'd want to know "how do I upgrade this."

Basis for the beats below: the real, implemented recipe
(`MamRecipeProvider.infusedApothecary()`), not a simulated GUI craft (no existing MAM scene
simulates the crafting-table grid — see task brief). The recipe:

```
#P#      # = mam:infused_living_rock (6 total)
 #       P = mam:apothecary (the T1 block itself, consumed)
###      → mam:infused_apothecary
```

Laid out flat on the base plate as a 3×3 footprint (matches the shape's geometry 1:1, easier to
read in Ponder's isometric view than a literal vertical crafting-grid mockup). Concrete block
positions (base plate is `configureBasePlate(0, 0, 5)`, all at y=1):

| Recipe cell | World pos | Block |
|---|---|---|
| top-left `#` | (1,1,1) | Infused Living Rock |
| top-center `P` | (2,1,1) | Apothecary (T1) |
| top-right `#` | (3,1,1) | Infused Living Rock |
| mid-center `#` | (2,1,2) | Infused Living Rock |
| bottom-left `#` | (1,1,3) | Infused Living Rock |
| bottom-center `#` | (2,1,3) | Infused Living Rock |
| bottom-right `#` | (3,1,3) | Infused Living Rock |

Beats, in order:

1. `showBasePlate()`, idle(10) — empty plate, same open as every existing scene.
2. Reveal the T1 Apothecary alone at (2,1,1) via `showSection(position(...))` — per
   ponder-guide §3/§7, revealing it alone first avoids it being visually lost once the six
   full-cube rock blocks appear around it. idle(15).
3. `text_1` (attachKeyFrame, pointAt topOf(2,1,1)): "A Tier 1 Apothecary can be upgraded once
   you have Infused Living Rock…" — states the precondition (T2 tier rock exists) before
   showing the shape.
4. Reveal the six Infused Living Rock blocks one at a time (loop, mirrors
   `pureDaisyStone`'s reveal-then-idle(8) pattern), in recipe-reading order (top row, then mid,
   then bottom row) — makes the goblet shape assemble visibly rather than popping in at once.
5. `text_2` (attachKeyFrame, pointAt topOf(2,1,3), one of the bottom-row rocks): "…arranged in
   the same goblet shape as any other tier: six rock blocks around the base." — ties the layout
   back to the shared goblet pattern from `magic/10_apothecary.md`, since every tier uses the
   identical shape and only the rock/secondary-ingredient types change.
6. Craft resolution: the six rock positions are set to air one at a time (short idle(4) between
   each, faster than the reveal — a craft consuming ingredients reads as quicker than placing
   them), then the center position (2,1,1) is set to `mam:infused_apothecary`'s default state,
   replacing the T1 block.
7. `text_3` (attachKeyFrame, pointAt topOf(2,1,1)): "The rock is consumed and the Apothecary is
   infused — more slots, the same goblet, one tier stronger." — names the concrete payoff (slot
   count increase per the design doc's tier table: 4+1 → 6+1) without re-explaining the fluid/
   ingredient mechanic itself, which is T1's own scene territory (not yet built, out of scope
   here).
8. Final idle(80), matching every other scene's closing hold before loop/exit.

Total text beats: 3 (`text_1`–`text_3`), textIndex starts at 1 per ponder-guide §4.

**Scope note:** does not show the in-world fluid/ingredient-throwing mechanic (that's T1's own,
not-yet-built scene) — this scene is scoped to the tier-upgrade craft only, per the task brief's
"gathering Infused Living Rock, placing the goblet, dropping the T1 Apothecary in" framing minus
the literal GUI simulation Ponder doesn't do in this codebase.
