---
type: design
status: wip
last-updated: 2026-06-30
links: "[[magic/00_energy]], [[20_verdant-path]], [[magic/20_altar]], [[11_magic-implementation-status]]"
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

| Tier                  | Built from             | Secondary ingredient                               | School gate    | Notes                                               | Texture notes                          | Slots        |
|-----------------------|------------------------|---------------------------------------------------|----------------|-----------------------------------------------------|----------------------------------------|--------------|
| Apothecary            | Any rock (`c:stones`)  | `mam:mystical_petals` OR `mam:mystical_mushrooms` | All schools T1 | **Implemented**                                     | Uses skin from Botania, unscaled 16=>32 | x + 1 (seed) |
| Infused Apothecary    | Infused Living Rock    | Apothecary                                        | All schools T2 | Not yet implemented                                 | Recolor, use living rock texture       | 4*x+1        |
| Sacred Apothecary     | Sacred Living Rock     | Infused Apothecary                                | Verdant T3     | Not yet implemented                                 | ... tint it green                      | 4*4*x+1      |
| Desecrated Apothecary | Desecrated Living Rock | Infused Apothecary                                | Dark T3        | Dark-flavour parallel — TBD with dark-school design | ... tint it purple                     | 4*4*x+1      |

**Progression note:** T1 uses any rock because the player has no Living Rock yet — you need to craft a Pure Daisy in the Apothecary, place it, and let it convert stone → Living Rock before you can build a Mana Pool. The Apothecary must be craftable before that loop begins.

**Implementation note (2026-06-30):** the shipped MAM recipe accepts `mam:mystical_petals` OR `mam:mystical_mushrooms` in the center slot (via NeoForge's `CompoundIngredient`), not petal-only as originally scoped here. Rationale: T1 is the bootstrap tier for *all* schools, and mushrooms are the Summoning-school ingredient type (see fluid table below) — gating the only Tier-1 station behind a Verdant-only item would block non-Verdant players from bootstrapping their own school. Revisit if this creates balance issues.

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

**Status:** not yet implemented. `ApothecaryBlockEntity.interact()` is a stub — no bucket-fill, ingredient-add, or recipe-match logic exists yet. See [[11_magic-implementation-status]] for tracking.

---
        
## Nox bootstrap

T2+ Apothecary filled with Blood or Lava slowly converts fluid → Nox, emitted via the Mana Spreader network into **any** nearby Mana Pool — whichever pool the Spreader is aimed at, not a dedicated one. This taints that pool per the standard Tainting rule ([[magic/00_energy]]).

Yield intentionally tiny (1 per tick, 10 ticks to empty an Apothecary) — bootstrap only, not a viable production source.

**Note:** as a player's infrastructure grows (multiple pools, later-game secondary systems), this still just taints whatever pool is targeted. A deliberate setup might route the Spreader at a purpose-built sacrificial pool instead of a main supply — that's a player choice, not something the mechanic needs to special-case.

## Open Questions

- [ ] **T1 base slot count (`x`)** — the Slots column above (`x+1`, `4*x+1`, `4*4*x+1`) is defined relative to a base value `x` that hasn't been pinned down yet. Set during balancing pass.
- [ ] **In-world recipe data format** — `ApothecaryRecipe` currently models `(petals: List<Ingredient>, reagent: Ingredient, output: ItemStack)` as a `Recipe<SingleRecipeInput>`, which doesn't fit the multi-petal-list + reagent matching this mechanic actually needs (`matches()` is hardcoded `false`). Needs rework before `interact()` can be implemented — manual matching against `RecipeManager.getAllRecipesFor(...)` instead of the `Recipe<>` interface is one option.

---

## Status

| Item                                                                                     | Status                                                          |
|------------------------------------------------------------------------------------------|-----------------------------------------------------------------|
| Apothecary fluid types (Water → Verdant, Lava → Summoning, Blood → Sanguine, Milk → TBD) | ✅ decided                                                       |
| Apothecary accepts any fluid; unknown fluids inert                                       | ✅ decided                                                       |
| T1 crafting recipe (any rock + petal/mushroom, goblet shape)                             | ✅ implemented                                                   |
| T1 crafting recipe accepts mushroom as alternate to petal                                | ✅ implemented (net-new, see implementation note above)          |
| In-world ingredient/catalyst mechanic                                                    | ⬜ not implemented — `interact()` stub                           |
| `ApothecaryRecipe` data model fit for multi-petal matching                               | ⬜ not implemented — current model assumes single-input matching |
| Infused / Sacred / Desecrated tiers                                                      | ⬜ not implemented                                               |
| Lava Apothecary school                                                                   | ⬜ TBD                                                           |
| Nox bootstrap via Apothecary (mechanic: T2+ + Blood/Lava → trickle Nox → taint targeted Mana Pool) | ✅ decided — Lava path viable now; Blood path blocked on Blood fluid implementation |
