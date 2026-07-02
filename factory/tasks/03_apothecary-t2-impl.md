---
type: task
gate: done
last-updated: 2026-07-02
links: ["[[magic/10_apothecary]]"]
---

# T2 Apothecary — Implementation

Implement the Infused Apothecary (T2) recipe: 6 Infused Living Rock in the goblet's stone slots +
a base Apothecary consumed in the center slot → Infused Apothecary. Mirrors T1's own
`MamRecipeProvider.apothecary()` recipe method — same goblet shape, tier-matched rock, output is
the next tier's block. Recipe ID should be `mam:infused_apothecary`, matching the tier-suffix
convention already used elsewhere (`infused_mana_pool`) and already forward-referenced by the
book-doc task (`02`).

Per `[[coder]]`, write the implementation and its own JUnit/GameTest coverage together in this
task — the human creates a separate `test` task (`04`) once this one is `done`, for [[tester]] to
audit that coverage; not folded into this task regardless of how simple the batch is (see
`[[agent-factory-guide]]` §1's Coder/Tester Q&A). See [[magic/10_apothecary]]'s Validation section
for the exact open item this closes ("Infused / Sacred / Desecrated Apothecary recipes — no recipe
methods exist beyond T1" — this task only covers T2; Sacred/Desecrated are separate future tasks).

## Handoffs

- 2026-07-02 — task created (human), gate: impl
- 2026-07-02 — implemented (coder), gate: done. Added `MamRecipeProvider.infusedApothecary()`
  (`src/main/java/org/mjli/mam/infrastructure/datagen/MamRecipeProvider.java`), mirroring
  `apothecary()` exactly in shape (`#P#`/` # `/`###`) but with `#` = `VerdantRock.INFUSED_LIVING_ROCK`
  and `P` = `Ingredient.of(VerdantMana.APOTHECARY.get())` (plain base-Apothecary ingredient, not the
  T1 petal/mushroom `CompoundIngredient`). Output `VerdantMana.INFUSED_APOTHECARY`, saved as
  `mam:infused_apothecary`. Registered the call alongside `apothecary(output)` /
  `apothecaryPureDaisy(output)` in `buildRecipes()`. Ran `runData` and confirmed the generated JSON
  (`src/generated/resources/data/mam/recipe/infused_apothecary.json`) matches the design doc's tier
  table exactly (6 rock + 1 apothecary → 1 infused apothecary).

  **Test (JUnit vs GameTest decision):** checked precedent first, per the task brief. T1's own
  `apothecary()` shaped recipe has no dedicated PA-numbered test in `TestApothecary` — the PA-1..5
  tests there cover the in-world fill/throw/catalyst mechanic (`ApothecaryBlockEntity`), not the
  crafting-table recipe that builds the station block itself. The actual precedent for
  tier-parallel shaped-recipe testing is `TestRecipes.java` (`RC-8`,
  `manaPoolCraftingRecipes` — same "4 tiers, same shape, tier-matched rock ingredient" pattern this
  task needed). In principle `[[gametest-guide]]`'s JUnit/GameTest split favors JUnit for pure
  recipe-matching logic with no live world/block-entity involved — but this codebase has no JUnit
  harness that bootstraps vanilla registries/`RecipeManager` (confirmed: `ManaPoolTest` and
  `EnergyNetworkHandlerTest`, the only two JUnit tests that exist, are pure math/state tests with
  zero registry access). Every existing recipe-matching check (`RC-1` through `RC-8`) is a GameTest
  precisely because `RecipeManager` needs live, datapack-loaded recipe data only available in the
  GameTest server context. Followed that precedent rather than the guide's abstract preference:
  added `infusedApothecaryCraftingRecipe` (`RC-9`, cross-referenced as `PA-6` in the docstring since
  that's the numbering the task brief anticipated) to `TestRecipes.java`, not `TestApothecary.java`.
  It looks up the recipe by result item, then asserts the ingredient list contains exactly 6
  ingredients matching `VerdantRock.INFUSED_LIVING_ROCK` and exactly 1 matching
  `VerdantMana.APOTHECARY` — stronger than `RC-8`'s existence-only check, since the task explicitly
  asked to verify "matches on the correct shaped-crafting input."

  **Verification:** `./gradlew compileJava` — clean. `./gradlew test` — all JUnit tests pass
  (no regressions in `ManaPoolTest`/`EnergyNetworkHandlerTest`, neither touched). `./gradlew runData`
  — recipe JSON generated and inspected, matches design doc. Did **not** run
  `./gradlew runGameTestServer` — no display/long-running capability in this session to drive or
  observe the GameTest server; `RC-9`/`infusedApothecaryCraftingRecipe` has not been executed, only
  compiled and read for correctness. This should be run before the coverage is treated as verified
  (natural first check for the downstream Tester audit task).

- 2026-07-02 — human ran `./gradlew runGameTestServer` to close the gap Coder flagged (RC-9
  compiled but unexecuted). Result: 68 game tests complete, `infusedApothecaryCraftingRecipe`
  (RC-9) **passed**. One unrelated required-test failure, `daybloomnomanawhenskyblocked` —
  pre-existing, already tracked in `todo/10_daybloom-sky-blocked-test-failure.md`, not a
  regression from this change. Recipe coverage for this task is now genuinely verified, not just
  reviewed.

  Composed, not executed (no commit rights at this gate):

  ```bash
  git add src/main/java/org/mjli/mam/infrastructure/datagen/MamRecipeProvider.java \
          src/main/java/org/mjli/mam/infrastructure/gametest/tests/TestRecipes.java

  git commit -m "$(cat <<'EOF'
  feat: infused apothecary T2 crafting recipe

  Mirrors T1's apothecary() goblet recipe: 6 Infused Living Rock + 1 base
  Apothecary (center slot) -> Infused Apothecary. Closes the T2 recipe gap
  flagged in design/magic/10_apothecary.md's Validation section. Adds RC-9
  in TestRecipes.java verifying the recipe matches on the correct shaped
  input (ingredient composition, not just output existence).
  EOF
  )"
  ```
