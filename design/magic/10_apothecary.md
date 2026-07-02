---
type: design
status: wip
last-updated: 2026-07-02
links: ["[[magic/00_energy]]", "[[20_verdant-path]]", "[[magic/20_altar]]", "[[magic/15_mana-pool]]"]
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
| Sacred Apothecary     | Sacred Living Rock     | Infused Apothecary                                | Verdant T3     | Not yet implemented                                 | ... tint it green                       | 64 + 1 (seed) |
| Desecrated Apothecary | Desecrated Living Rock | Infused Apothecary                                | Dark T3        | Dark-flavour parallel — TBD with dark-school design | ... tint it purple                      | 64 + 1 (seed) |

**Balancing note (2026-07-02):** T3 slot count increased 8 → 16 → 64 (one full stack — no T3 recipe changes), matching the same increases applied to the [[magic/20_altar]] T3 tier. No current recipe needs more than 8 slots; this is headroom for future endgame recipes and likely relevant once the [[magic/15_mana-pool]] tier work lands.

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
4. Wrong ingredient thrown in? Right-click empty-handed to retract — pops the most recently thrown ingredient back to the player's inventory, one per click (LIFO), so an out-of-order mistake comes back first without undoing earlier correct throws.
5. Just crafted and want to do it again? Refill the fluid, then right-click empty-handed — for 20 seconds after a craft, this pulls the same ingredients back out of the player's inventory (best-effort: whatever the player is carrying, one item per slot pass, not all-or-nothing) instead of retracting.

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

**Q:** How should the fluid actually be modeled and rendered, to fix the "zero visible effect" gap below?
**A:** Replaced the `FluidState{EMPTY,WATER,LAVA}` enum with a real NeoForge `IFluidHandler`/`FluidTank` capability (1 bucket capacity, no validator — any bucket-compatible fluid accepted, matching "unrecognized fluids inert" above), chosen over a simpler plain-int counter (Botania's mana-pool approach) so the tank has genuine interop potential with hoppers/pipes/other mods. Rendering is a continuous fill-fraction height lerp adapted from Botania's `ManaPoolBlockEntityRenderer` pattern, generalized from mana-only to any registered fluid via `IClientFluidTypeExtensions` (sprite + tint resolved per-fluid, not hardcoded to water/lava) — confirmed in-game working for water, lava, and milk. Two non-obvious pitfalls hit during implementation, worth flagging for anyone touching this again: (1) block-entity data isn't synced to the client by default — `setChanged()` only marks the chunk dirty for disk saving, so `getUpdateTag()`/`getUpdatePacket()` overrides plus a `level.sendBlockUpdated(...)` call were required before the client-side copy ever saw the fluid; (2) `TextureAtlasSprite.getU/getV` take a 0-1 sprite fraction in this MC version, not the 0-16 pixel units Botania's (1.20.1) reference code uses — passing raw pixel values sampled outside the sprite into neighboring atlas content.

**Q:** Desecrated Apothecary — what does the "dark-flavour parallel" T3 tier actually look like (recipes, art, mechanic differences from Sacred)?
**A:** Out of scope for this doc — per-school dark design. Not this doc's blocker; resolved when a dark-school doc exists.

---

## Validation

- `done` — T1 crafting recipe (goblet shape, `c:stones` + petal/mushroom via `CompoundIngredient`) — [`MamRecipeProvider.apothecary()`](../../src/main/java/org/mjli/mam/infrastructure/datagen/MamRecipeProvider.java)
- `done` — NBT roundtrip (`FluidState` + petal list survive save/load) — tested by `apothecaryNbtRoundtrip` (PA-2) in [`TestApothecary`](../../src/main/java/org/mjli/mam/infrastructure/gametest/tests/TestApothecary.java)
- `done` — In-world ingredient/catalyst mechanic — [`ApothecaryRecipe`](../../src/main/java/org/mjli/mam/recipe/ApothecaryRecipe.java) is now `RecipeWithReagent extends Recipe<RecipeInput>` with a greedy `matches()` (Botania `PetalsRecipe` pattern, adapted to MC 1.21.1's split between `Container` and the newer minimal `RecipeInput` — Botania's own `Recipe<Container>` reference doesn't compile as-is on this version, so matching runs against a small [`ApothecaryInput`](../../src/main/java/org/mjli/mam/recipe/ApothecaryInput.java) `RecipeInput` wrapper instead); [`ApothecaryBlockEntity.tick()`](../../src/main/java/org/mjli/mam/block_entity/ApothecaryBlockEntity.java) scans an AABB above the block each tick and feeds colliding `ItemEntity`s into `collideEntityItem()`, which accumulates ingredients, matches against `RecipeManager`, and crafts + drains the fluid the instant a thrown item satisfies the matched recipe's reagent. `interact()` now only handles empty-hand (`PASS`) and bucket-fill (water/lava bucket → `WATER`/`LAVA`, `SUCCESS`) per the Open Question resolution above — ingredient/reagent handling moved entirely to the tick loop. Tested end-to-end by `apothecaryCraftsPureDaisyFromPetalsAndSeed` (PA-3): fills with water, throws 4 white petals + a seed reagent, asserts the Pure Daisy output spawns and the container/fluid reset.
- `done` — T1 bootstrap recipe content — `mam:apothecary/pure_daisy` (4× white petal + `#c:seeds` reagent → Pure Daisy), ported from Botania's `petal_apothecary/pure_daisy.json` — [`MamRecipeProvider.apothecaryPureDaisy()`](../../src/main/java/org/mjli/mam/infrastructure/datagen/MamRecipeProvider.java). This was the only recipe blocking the T1 slot-count fix above from being verifiable.
- `done` — Fluid visual feedback — `ApothecaryBlockEntity` now holds a real `FluidTank` (1 bucket, any fluid) exposed as a NeoForge `IFluidHandler` capability ([`MamCapabilities`](../../src/main/java/org/mjli/mam/MamCapabilities.java)), and [`ApothecaryBlockEntityRenderer`](../../src/main/java/org/mjli/mam/client/render/ApothecaryBlockEntityRenderer.java) draws a continuous fill-fraction quad in the goblet basin, sprite/tint resolved per-fluid via `IClientFluidTypeExtensions`. Confirmed in-game for water, lava, and milk (2026-07-01). See Open Question above for the full design rationale and two version-specific pitfalls hit along the way.
- `done` — Ingredient orbit rendering — ingested petals/mushrooms had no visual presence: their `ItemEntity` is discarded on ingestion ([`ApothecaryBlockEntity.collideEntityItem`](../../src/main/java/org/mjli/mam/block_entity/ApothecaryBlockEntity.java)), same as Botania's `PetalApothecaryBlockEntity`, and nothing rendered the stored stacks. `ApothecaryBlockEntity.getPetals()` renamed to `getIngredients()` (T1 accepts petals *or* mushrooms via `CompoundIngredient`, so "petals" was the wrong name for what the field actually holds) — same rename applied to the `petals`/`ingredients` NBT tag and the block entity's internal list; `ApothecaryRecipe`'s own `petals` field is untouched (separate, Botania-ported name tied to the recipe JSON schema). `ApothecaryBlockEntityRenderer.renderIngredients()` now draws each stored stack orbiting above the fluid surface, mirroring Botania's `PetalApothecaryBlockEntityRenderer` (which also discards the entity and renders the stored stacks itself, not the live entity) — but rendered full-bright (`LightTexture.FULL_BRIGHT`) rather than lit from world position: these are flat 2D icons, not solid blocks, and an initial attempt at resampling light per-item caused visible blinking as the bob animation crossed the block-above boundary each cycle. Scale tuned up twice from the initial Botania-derived value (`0.15F` → `0.22F` → `0.32F`) for legibility. PA-2/PA-3 GameTests (NBT roundtrip, craft logic) still pass after the rename. Confirmed in-game (2026-07-02) — see [`test/00_apothecary-ingredient-orbit.md`](../../test/00_apothecary-ingredient-orbit.md).
- `done` — Ingredient retraction + recraft — empty-hand right click was previously a no-op beyond bucket-fill; [`ApothecaryBlockEntity.interact()`](../../src/main/java/org/mjli/mam/block_entity/ApothecaryBlockEntity.java) now checks in order: (1) `canAddLastRecipe()` — ingredients empty, fluid refilled, within the 20s post-craft window — pulls the prior ingredient list back out of the player's inventory via `trySetLastRecipe()`; (2) non-empty `ingredients` — pops the last element back to the player via `placeItemBackInInventory()` (LIFO, one per click); (3) falls through to the existing `FluidUtil` bucket logic. Ported from Botania's `PetalApothecaryBlock.use()` / `InventoryHelper.withdrawFromInventory()` / `InventoryHelper.tryToSetLastRecipe()` (`Xplat/.../common/block/PetalApothecaryBlock.java:118-136`, `.../helper/InventoryHelper.java:104-115,156-186`) — same LIFO-retract and best-effort-refill patterns. `lastRecipe`/`recipeKeepTicks` are deliberately transient (not saved to NBT), matching Botania's own comment that this state isn't synced or persisted — it's a same-session convenience, not durable state. Covered by GameTests PA-4 (retract, LIFO order) and PA-5 (recraft, pulls matching items from player inventory) in [`TestApothecary`](../../src/main/java/org/mjli/mam/infrastructure/gametest/tests/TestApothecary.java); all 66 GameTests pass.
- `todo` — Infused / Sacred / Desecrated Apothecary recipes — no recipe methods exist beyond T1 in `MamRecipeProvider`
- `todo` — Nox bootstrap via Apothecary — zero implementation. [`ApothecaryBlockEntity.FluidState`](../../src/main/java/org/mjli/mam/block_entity/ApothecaryBlockEntity.java) only has `EMPTY`/`WATER`/`LAVA` — no `BLOOD` or `MILK` entries at all, despite the fluid table above listing four fluids
