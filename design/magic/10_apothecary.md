---
type: design
status: wip
last-updated: 2026-07-01
links: ["[[magic/00_energy]]", "[[20_verdant-path]]", "[[magic/20_altar]]"]
---

# MAM — Apothecary

Cross-school crafting station. Any school can register Apothecary recipes; fluid type selects which school's recipes are active. See [[magic/00_energy]] for the broader energy/station model this fits into, and [[magic/20_altar]] for the parallel station that uses pool energy instead of fluid.

---

## Crafting the Apothecary

All tiers share the same goblet pattern — 7 rock blocks + 1 petal (or mushroom, see implementation note below). The tier material is the only gate; the center ingredient slot stays constant.

```
C P C
. C .   C = rock (tier-matched)   P = petal or mushroom
C C C
```

| Tier                  | Built from             | Secondary ingredient                              | School gate    | Notes                                               | Texture notes                           | Slots        |
|-----------------------|------------------------|---------------------------------------------------|----------------|-----------------------------------------------------|-----------------------------------------|--------------|
| Apothecary            | Any rock (`c:stones`)  | `mam:mystical_petals` OR `mam:mystical_mushrooms` | All schools T1 | **Implemented**                                     | Uses skin from Botania, unscaled 16=>32 | 4 + 1 (seed) |
| Infused Apothecary    | Infused Living Rock    | Apothecary                                        | All schools T2 | Not yet implemented                                 | Recolor, use living rock texture        | 6 + 1 (seed) |
| Sacred Apothecary     | Sacred Living Rock     | Infused Apothecary                                | Verdant T3     | Not yet implemented                                 | ... tint it green                       | 8 + 1 (seed) |
| Desecrated Apothecary | Desecrated Living Rock | Infused Apothecary                                | Dark T3        | Dark-flavour parallel — TBD with dark-school design | ... tint it purple                      | 8 + 1 (seed) |

**Progression note:** T1 uses any rock because the player has no Living Rock yet — you need to craft a Pure Daisy in the Apothecary, place it, and let it convert stone → Living Rock before you can build a Mana Pool. The Apothecary must be craftable before that loop begins.

**Implementation note (2026-06-30):** the shipped MAM recipe accepts `mam:mystical_petals` OR `mam:mystical_mushrooms` in the center slot (via NeoForge's `CompoundIngredient`), not petal-only as originally scoped here. Rationale: T1 is the bootstrap tier for *all* schools, and mushrooms are the Summoning-school ingredient type (see fluid table below) — gating the only Tier-1 station behind a Verdant-only item would block non-Verdant players from bootstrapping their own school. Revisit if this creates balance issues.

**Implementation note (2026-07-01):** T1 slot count corrected from 3 to 4. The bootstrap Pure Daisy recipe ported directly from Botania (`petal_apothecary/pure_daisy.json`) needs 4 white petals + 1 seed reagent thrown last; a 3-slot cap couldn't fit the real reference recipe this doc itself points to. The original "3" was a transcription slip, not a deliberate balance choice.

*Reference: `Botania/Xplat/src/generated/resources/data/botania/recipes/apothecary_livingrock.json`*

---

## Using the Apothecary

The Apothecary accepts any bucket-compatible fluid (stationary). Fluid type activates a recipe school; unrecognized fluids are accepted but inert.

| Fluid | Colour | Recipe school    | Ingredient type | Catalyst |
|-------|--------|------------------|-----------------|----------|
| Water | Blue   | Verdant / floral | Petals          | Seed     |
| Lava  | Orange | Summoning        | Mushrooms       | Seed     |
| Blood | Red    | Sanguine         | TBD             | Seed     |
| Milk  | White  | TBD              | TBD             | TBD      |
| Other | —      | —                | —               | —        |

**Mechanic** (same across all schools):
1. Fill the Apothecary with the school fluid.
2. Throw ingredients in — they float/circle above the fluid.
3. Throw the catalyst last — triggers recipe matching and ejects the output item.

Note: drains the fluid needs to be refilled with water/lava/blood after each crafting ...                                 

Tier gates the number of ingredient slots (how many items can float simultaneously), not the school. A single Apothecary block serves all schools by swapping fluid.

---
        
## Nox bootstrap

T2+ Apothecary filled with Blood or Lava slowly converts fluid → Nox, emitted via the Mana Spreader network into **any** nearby Mana Pool — whichever pool the Spreader is aimed at, not a dedicated one. This taints that pool per the standard Tainting rule ([[magic/00_energy]]).

Yield intentionally tiny (1 per tick, 10 ticks to empty an Apothecary) — bootstrap only, not a viable production source.

**Note:** as a player's infrastructure grows (multiple pools, later-game secondary systems), this still just taints whatever pool is targeted. A deliberate setup might route the Spreader at a purpose-built sacrificial pool instead of a main supply — that's a player choice, not something the mechanic needs to special-case.

## Open Questions

**Q:** In-world recipe data format — `ApothecaryRecipe` currently models `(petals: List<Ingredient>, reagent: Ingredient, output: ItemStack)` as a `Recipe<SingleRecipeInput>`, which doesn't fit the multi-petal-list + reagent matching this mechanic actually needs (`matches()` is hardcoded `false`).
**A:** Adopt Botania's Petal Apothecary pattern directly (this doc's own crafting-recipe reference, `Botania/Xplat/.../apothecary_livingrock.json`, is already a port of the same station). `ApothecaryRecipe` becomes `Recipe<Container>` — drop `SingleRecipeInput` entirely, no custom `RecipeInput` class needed — with a `getReagent(): Ingredient` accessor alongside it, mirroring Botania's `RecipeWithReagent extends Recipe<Container>` (`Xplat/.../api/recipe/RecipeWithReagent.java`). `matches()` becomes a greedy, order-independent walk: for each item in the container, cross off the first still-unmatched `Ingredient` it satisfies; fail immediately on any item with no match; succeed only when every ingredient is crossed off (exact count — no extras, no shortfall). Duplicate ingredients (e.g. two-of-any-petal) just work, since each is its own list entry. Reference: `PetalsRecipe.matches()` (`Xplat/.../common/crafting/PetalsRecipe.java:56-83`). Matching itself is plain vanilla `RecipeManager.getRecipeFor(type, container, level)` — no manual iteration.

This also settles the trigger mechanism, and reframes where the logic belongs: Botania has no click-based "throw catalyst last" step in code. Its block entity scans colliding `ItemEntity`s every tick (`PetalApothecaryBlockEntity.collideEntityItem()`/`serverTick()`), accumulating thrown items into a `SimpleContainer`, and checks each newly-arriving item against the *current* match's reagent directly — decoupled from the container being matched. This is exactly the "throw ingredients in — they float above the fluid" mechanic already described above, not a right-click interaction. So `interact()` is the wrong extension point for the real mechanic going forward — it should stay relevant only for empty-hand/bucket-fill, with matching logic living in a per-tick collision handler instead. (2026-07-01)

**Q:** Milk — which school (if any) claims this fluid? Recipe school, ingredient type, and catalyst are all undecided (see fluid table above). Unlike Blood, nothing currently owns this — it isn't scoped to an existing per-school doc.
**A:** Out of scope for this doc — this is left open for future expansion

**Q:** Blood — what item is the Sanguine-school ingredient type (the parallel to Petals/Mushrooms)?
**A:** Out of scope for this doc — Sanguine school design. Not this doc's blocker; resolved when the Sanguine school doc exists.

**Q:** Desecrated Apothecary — what does the "dark-flavour parallel" T3 tier actually look like (recipes, art, mechanic differences from Sacred)?
**A:** Out of scope for this doc — per-school dark design. Not this doc's blocker; resolved when a dark-school doc exists.

---

## Validation

- `done` — T1 crafting recipe (goblet shape, `c:stones` + petal/mushroom via `CompoundIngredient`) — [`MamRecipeProvider.apothecary()`](../../src/main/java/org/mjli/mam/infrastructure/datagen/MamRecipeProvider.java)
- `done` — NBT roundtrip (`FluidState` + petal list survive save/load) — tested by `apothecaryNbtRoundtrip` (PA-2) in [`TestApothecary`](../../src/main/java/org/mjli/mam/infrastructure/gametest/tests/TestApothecary.java)
- `done` — In-world ingredient/catalyst mechanic — [`ApothecaryRecipe`](../../src/main/java/org/mjli/mam/recipe/ApothecaryRecipe.java) is now `RecipeWithReagent extends Recipe<RecipeInput>` with a greedy `matches()` (Botania `PetalsRecipe` pattern, adapted to MC 1.21.1's split between `Container` and the newer minimal `RecipeInput` — Botania's own `Recipe<Container>` reference doesn't compile as-is on this version, so matching runs against a small [`ApothecaryInput`](../../src/main/java/org/mjli/mam/recipe/ApothecaryInput.java) `RecipeInput` wrapper instead); [`ApothecaryBlockEntity.tick()`](../../src/main/java/org/mjli/mam/block_entity/ApothecaryBlockEntity.java) scans an AABB above the block each tick and feeds colliding `ItemEntity`s into `collideEntityItem()`, which accumulates ingredients, matches against `RecipeManager`, and crafts + drains the fluid the instant a thrown item satisfies the matched recipe's reagent. `interact()` now only handles empty-hand (`PASS`) and bucket-fill (water/lava bucket → `WATER`/`LAVA`, `SUCCESS`) per the Open Question resolution above — ingredient/reagent handling moved entirely to the tick loop. Tested end-to-end by `apothecaryCraftsPureDaisyFromPetalsAndSeed` (PA-3): fills with water, throws 4 white petals + a seed reagent, asserts the Pure Daisy output spawns and the container/fluid reset.
- `done` — T1 bootstrap recipe content — `mam:apothecary/pure_daisy` (4× white petal + `#c:seeds` reagent → Pure Daisy), ported from Botania's `petal_apothecary/pure_daisy.json` — [`MamRecipeProvider.apothecaryPureDaisy()`](../../src/main/java/org/mjli/mam/infrastructure/datagen/MamRecipeProvider.java). This was the only recipe blocking the T1 slot-count fix above from being verifiable.
- `todo` — Fluid visual feedback — filling with water/lava via `interact()` changes `ApothecaryBlockEntity.FluidState` but has **zero visible effect in-game**: no `BlockState` property carries fluid (unlike Botania's `PetalApothecaryBlock.FLUID` blockstate property + per-state model), and there's no `BlockEntityRenderer`. Mechanically correct (confirmed via GameTest and manual bucket-fill), but unusable by feel until fluid is exposed visually. Deferred — surfaced during in-game verification on 2026-07-01, not yet scoped.
- `todo` — Infused / Sacred / Desecrated Apothecary recipes — no recipe methods exist beyond T1 in `MamRecipeProvider`
- `todo` — Nox bootstrap via Apothecary — zero implementation. [`ApothecaryBlockEntity.FluidState`](../../src/main/java/org/mjli/mam/block_entity/ApothecaryBlockEntity.java) only has `EMPTY`/`WATER`/`LAVA` — no `BLOOD` or `MILK` entries at all, despite the fluid table above listing four fluids
