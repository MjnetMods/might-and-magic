---
type: task
gate: merge
last-updated: 2026-07-03
links: ["[[magic/10_apothecary]]"]
---

# T2 Apothecary — Review

Review the complete T2 Apothecary change once `03` (impl), `04` (test), and `05` (ponder-doc) are
all done — the recipe implementation, its audited test coverage, and the ponder scene built
against the real, testable mechanic. Reviewing after test and ponder rather than straight off impl
is deliberate for this task order: both sit between impl and review (not after review) so the
review pass covers the finished feature as a player will actually encounter it, not just the code
diff in isolation.

## Handoffs

- 2026-07-02 — task created (human), gate: review
- 2026-07-02 — reviewed (reviewer), gate: review → bounced to Coder. Diff reviewed: `03` impl
  (commit `3a856a4`) + `04` test (commit `8d12a45`) against `design/magic/10_apothecary.md`'s tier
  table and the `01`/`02` site-doc/book-doc tasks. Two findings, both `CONFIRMED`:
  1. **Real bug** — the design doc, site doc, and book doc all promise the Infused Apothecary (T2)
     gets 6 ingredient slots vs. T1's 4, but `ApothecaryBlockEntity.MAX_INGREDIENTS` was a single
     hardcoded `4` shared by every tier's block entity (confirmed by grep — zero tier branching). A
     crafted T2 block behaved identically to T1; the recipe worked but the payoff it's supposed to
     unlock didn't exist. Coder's and Tester's own `03`/`04` handoff logs had already surfaced "no
     tier-specific branching" as a neutral fact (no regression risk) without connecting it back to
     what the docs promised the player — exactly the check this charter's Directive calls out.
  2. **Minor** — `design/magic/10_apothecary.md`'s Validation line for the Infused/Sacred/
     Desecrated recipes still read `todo`, stale since T2's recipe was actually done and tested.
- 2026-07-02 — fixed (coder), gate: review (pending re-verification). Root cause: unlike
  `ManaPoolBlockEntity` (separate `BlockEntityType` per tier, each constructed with its own
  capacity constant — the established precedent for tiered capacity in this codebase),
  `ApothecaryBlockEntity` shares one `BlockEntityType` across all four tier blocks via a no-arg
  constructor, so no tier signal ever reached it. Fix: `ApothecaryBlockEntity`'s constructor now
  resolves capacity from `state.getBlock()` (already available at construction) against
  `VerdantMana`'s tier blocks — T1/`APOTHECARY` = 4, `INFUSED_APOTHECARY` = 6,
  `SACRED_APOTHECARY`/`DESECRATED_APOTHECARY` = 64, matching the design doc's tier table exactly.
  Chose this over splitting into 4 `BlockEntityType`s (Mana Pool's approach) as the narrower fix —
  avoids rippling into `MamBlockEntities` registration, capability wiring, and existing GameTest
  block-entity casts, none of which need to change for this bug.

  Added `apothecaryTier2AcceptsSixIngredients` (PA-6) to `TestApothecary.java` — throws 7 generic
  items at an `INFUSED_APOTHECARY` block entity and asserts exactly 6 are accepted with the 7th
  left uningested; this test would have failed under the old hardcoded-4 cap, so it's a genuine
  regression guard, not a restatement of the fix. Also closed finding 2: updated
  `design/magic/10_apothecary.md`'s Validation section — split the old combined `todo` line into a
  `done` line for T2 (cross-referencing RC-9/RC-10/PA-6) and a `todo` line for the still-unbuilt
  Sacred/Desecrated recipes.

  **Verification:** `./gradlew compileJava compileTestJava` — clean. Did **not** run
  `./gradlew runGameTestServer` — same no-display/long-running constraint every prior handoff in
  this task chain has flagged. PA-6 is compiled and read for correctness only, not executed yet.
  Flagging for the human to run alongside a re-review before this task can move to `merge`.

- 2026-07-03 — human ran `./gradlew runGameTestServer`: 1 required test failed,
  `apothecarytier2acceptssixingredients` (PA-6). Root cause was in the **test, not the capacity
  fix**: `collideEntityItem`'s very first guard (`if (stack.isEmpty() || tank.isEmpty()) return
  false;`) rejects every ingredient outright while the fluid tank is empty — true for every tier,
  unchanged by this task. PA-3/PA-4/PA-5 all fill the tank with a water bucket before throwing
  ingredients; PA-6 skipped that setup step, so all 7 thrown sticks were rejected and
  `getIngredients()` came back `0`, not `6`. Fixed by adding the same
  `player.setItemInHand(WATER_BUCKET); be.interact(player);` fill step PA-3/4/5 already use, before
  throwing the 7 sticks. Re-ran `./gradlew runGameTestServer`: **all 70 required tests pass**,
  including PA-6. The per-tier capacity fix itself was correct from the start; only the regression
  test guarding it needed correcting. Still open before `merge`: a re-review of this test fix (not
  yet done by a Reviewer pass, only self-verified by execution).
- 2026-07-03 — re-reviewed (reviewer), both fix commits (`3f061fc`, `690878d`) together. Verified,
  not just read: `capacityFor()`'s block-identity branches match `design/magic/10_apothecary.md`'s
  tier table exactly (T1=4, T2=6, Sacred=64, Desecrated=64); PA-6's fill→throw×7→wait(2
  ticks)→assert sequence correctly distinguishes capacity=6 from both "still hardcoded to 4" and
  "unbounded," using the same batch-spawn-then-`runAfterDelay` timing pattern PA-3/4/5 already
  prove reliable — and the actual 70/70 passing run is empirical confirmation of that, not just a
  read-through. One finding, doc-only: `design/magic/10_apothecary.md`'s tier *table* (not the
  Validation section, which was already correct) still had Infused Apothecary's row marked "Not
  yet implemented" / "Blocked on this tier existing as a block" — stale on both counts, since the
  block, recipe, and capacity are all implemented, and the block is already wired into the shared
  per-tier tint mechanism in `MightAndMagicClient.java` (confirmed by grep — `INFUSED_APOTHECARY`
  appears in the T2 tint-target array alongside its Mana Pool/Altar siblings). Fixed: row updated
  to `**Implemented**`, texture note updated to describe the tint as applied rather than blocked.
  No bounce to Coder/Tester — this was the only gap, and it's closed now.

  **Verdict: ready for `merge`.** Both fix commits are correct and tested; the one doc
  inconsistency this pass found has been corrected in the same pass.
