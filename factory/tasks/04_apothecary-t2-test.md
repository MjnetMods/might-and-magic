---
type: task
gate: done
last-updated: 2026-07-02
links: ["[[magic/10_apothecary]]"]
---

# T2 Apothecary — Test Audit

Created once `03` (impl) is `done`. Owned by [[tester]] — audit Coder's JUnit/GameTest coverage
for the Infused Apothecary recipe against [[magic/10_apothecary]]'s stated behavior, close any
real gaps found, and flag a manual verification note under `test/` only as a last resort (see
[[tester]]'s charter for why manual notes are last-resort, not default). Not conditioned on this
being a "simple" mechanical batch — this checkpoint always runs.

## Handoffs

- 2026-07-02 — task created (human), gate: test
- 2026-07-02 — audited (tester), gate: done. Walked design/magic/10_apothecary.md's tier table and
  Validation section against Coder's diff (`MamRecipeProvider.infusedApothecary()`, RC-9
  `infusedApothecaryCraftingRecipe` in `TestRecipes.java`). Findings:

  - **Real gap found and closed:** RC-9 asserts ingredient *composition* (6x Infused Living Rock +
    1x base Apothecary) by counting `Ingredient.test()` matches over `ShapedRecipe.getIngredients()`
    — confirmed by reading vanilla `ShapedRecipe.getIngredients()` source, this returns
    `pattern.ingredients()`, the pattern's flat positional list (blank cells included), not a
    deduplicated set. Counting matches over that list verifies *counts*, not *position* — a
    transposition bug (e.g. apothecary moved from top-center to a corner, rock filling the gap)
    would keep the same aggregate counts and RC-9 would still pass. Closed by adding
    `infusedApothecaryRequiresGobletShape` (RC-10) to `TestRecipes.java`: builds two real 3x3
    `CraftingInput` grids and drives them through `RecipeManager.getRecipeFor` (the actual matching
    path, not a manual ingredient scan) — one with the correct `#P#`/` # `/`###` layout (must
    match), one with the same 6 rock + 1 apothecary but the apothecary shifted to top-left (must
    not match). This is the same pattern used by `ApothecaryBlockEntity`'s own recipe lookup
    (`getRecipeFor`), so it exercises real matching semantics, not a hand-rolled proxy for them.
  - **Output identity:** confirmed real, not just claimed — RC-9 filters by
    `getResultItem(registries).is(infusedApothecary)` against `VerdantMana.INFUSED_APOTHECARY`,
    read directly in `TestRecipes.java:185-189`.
  - **Block-entity behavior:** confirmed the design doc's "no different from the base Apothecary"
    claim is actually true rather than assumed — `ApothecaryBlockEntity` has zero tier-specific
    branching (grepped for `tier`/`Tier`/`INFUSED_APOTHECARY`, only hit is a comment on T1's
    capacity constant), and `VerdantMana.INFUSED_APOTHECARY` registers the same `ApothecaryBlock`
    class as T1. So PA-1..PA-5's existing in-world coverage in `TestApothecary` already applies
    generically to the T2 block; no new GameTest needed here.
  - **T1 regression risk:** checked directly rather than assumed — read the generated
    `src/generated/resources/data/mam/recipe/{apothecary,infused_apothecary}.json`: distinct IDs
    (`mam:apothecary` vs `mam:infused_apothecary`), T1's own JSON (stones + petal/mushroom
    compound, output `mam:apothecary`) is byte-for-byte what it was before this task. No ID
    collision, no registration-order effect from adding `infusedApothecary()` to `buildRecipes()`.
  - **Aside, out of scope for this task:** T1's own `apothecary()` shaped recipe (the goblet
    itself, not the Pure Daisy in-world recipe) has no dedicated crafting-table GameTest of its
    own — design doc's Validation line for it links only to the impl method, no test. Pre-existing
    gap from a prior task, not introduced or worsened here; not fixed in this task since this task
    is scoped to the T2 audit specifically (`[[tester]]`'s charter: doesn't touch other tasks'
    scope). Flagging in case a future task wants to close it.
  - No manual verification (`test/NN_*.md`) needed — everything above is checkable by GameTest;
    nothing here is rendering/visual/timing-dependent.

  **Verification:** `./gradlew compileJava` — clean, RC-10 compiles. Did **not** run
  `./gradlew runGameTestServer` — no display/long-running capability in this session, same
  constraint Coder flagged for RC-9. RC-10 is compiled and read for correctness only, not executed.
  Flagging for the human to run alongside/after RC-9's already-confirmed pass (68 tests + RC-9
  passing, per the impl task's handoff log) — this should be the natural next check before treating
  T2 coverage as fully verified.

- 2026-07-02 — human ran `./gradlew runGameTestServer` to close the gap Tester flagged (RC-10
  compiled but unexecuted). Result: 69 game tests complete, **all required tests passed**,
  including RC-10 and RC-9 together. `daybloomnomanawhenskyblocked` (the pre-existing tracked
  failure) passed this run too — looks order/flake-dependent rather than deterministic, but that's
  `todo/10_daybloom-sky-blocked-test-failure.md`'s concern, not this task's. T2 Apothecary test
  coverage is now fully verified end-to-end: composition (RC-9) and shape (RC-10) both confirmed
  passing against the real `RecipeManager`.

  Composed, not executed (no commit rights at this gate):

  ```bash
  git add src/main/java/org/mjli/mam/infrastructure/gametest/tests/TestRecipes.java

  git commit -m "$(cat <<'EOF'
  test: verify infused apothecary recipe enforces goblet shape

  RC-9 (added in the T2 impl task) checks ingredient composition (6x
  Infused Living Rock + 1x base Apothecary) by counting matches over
  ShapedRecipe.getIngredients(), which is the pattern's flat positional
  list -- so a transposition bug with the same aggregate counts would
  still pass. Adds RC-10, infusedApothecaryRequiresGobletShape: drives
  two real 3x3 CraftingInput grids through RecipeManager.getRecipeFor,
  confirming the correct #P#/ # /### layout matches and a
  same-composition-but-wrong-position layout does not.
  EOF
  )"
  ```
