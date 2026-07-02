---
type: design
status: wip
last-updated: 2026-07-02
links: ["[[20_verdant-path]]", "[[21_verdant-implementation-status]]", "[[magic/10_apothecary]]", "[[magic/00_energy]]", "[[magic/20_altar]]", "[[magic/25_runes]]"]
---

# Verdant — Generating Flowers

Verdant generating flowers turn an environmental or item trigger into mana, which accumulates on the flower and drains into the nearest bound Mana Pool. This doc covers all of them, organized into three power tiers — T1/T2/T3 — based on what a flower *does* for the player (payout size, setup investment), not on how hard its trigger logic is to implement. Implementation difficulty is a separate, orthogonal concern: see [[../todo/03_complex-generating-flower-mechanics]] for the four flowers whose mechanic implementation is deferred regardless of tier.

**T1** — Daybloom, Endoflame, Hydroangeas (shipped). Passive/low-maintenance triggers, no rune required.
**T2** — Thermalily, Rosa Arcana, Munchdew, Narslimmus. Require an existing side-activity (a lava/water rig, a mob farm, a slime farm, XP grinding) for moderate payout.
**T3** — Kekimurus, Gourmaryllis, Spectrolus, Entropinnyum, Dandelifeon, Shulk Me Not. Heavy logistics (cake supply, food farming, sheep/dye farms, TNT rigs, End access) for large payouts.

**Not ported**: Rafflowsia — depends on Botania's large-flower multiblock growth system, which MAM has no equivalent of. See [[../todo/02_rafflowsia-multiblock-flower]].

---

## Shared mechanic

Every generating flower is a `GeneratingFlowerBlock` / `GeneratingFlowerBlockEntity` pair:

- Each tick, the flower binds to the nearest Mana Pool within a 6-block radius (`BIND_RADIUS`), rebinding automatically if the bound pool goes away.
- `tickFlower()` — the one method each flower overrides — runs its own trigger check and calls `addMana(amount)`, capped at `getMaxEnergy()`.
- Once mana has accumulated, it drains into the bound pool every tick (as long as the pool isn't full), then resets to 0.

A new flower only needs to define its trigger condition and its `getMaxEnergy()` cap — the pool-binding and drain logic is shared.

---

## Recipe model

**Q:** Botania's real petal-apothecary recipes for most of these flowers require Runes (`rune_fire`, `rune_water`, `rune_gluttony`, `rune_mana`, `rune_summer`, `rune_winter`, `rune_air`, `rune_wrath`, `rune_envy`) and a few also need `pixie_dust`, `life_essence`, or `redstone_root` — all products of Botania's Runic Altar. Does MAM have an equivalent, and should recipes wait on it?
**A:** MAM already has a designed equivalent — [[magic/20_altar]] is explicitly "what the Runic Altar is to Botania," and [[magic/25_runes]] defines a full MAM-native rune taxonomy (`rune_life`, `rune_flow`, `rune_mana`, etc.) — but neither is implemented yet (both docs' Status tables show item registration/recipe JSONs "not started"). MAM's taxonomy is conceptually its own (Life/Death/Order/Chaos/Flow/Force primitives), not a re-skin of Botania's elemental/emotion runes, so these flower recipes were never going to import Botania's specific rune items 1:1 regardless. (2026-07-02)

**Q:** Given that, should flower recipes use MAM runes at all, or stay permanently petals-only?
**A:** Use MAM runes — tier-matched (T1 flowers stay petal-only; T2/T3 flowers get a thematically-matched rune per the tables below), scoped to a fixed ingredient-count ladder rather than copying Botania's own varying counts (Botania recipes range 3–11 ingredients per flower). MAM's shape: **T1 = 4 petals + seed. T2 = T1 + 2 more (typically 1 rune + 1 filler) = 6 + seed. T3 = T2 + 2 more (typically 1 rune + 3 filler total) = 8 + seed.** This reuses the same 4/6/8 ladder the Apothecary/Altar station tiers already use, and composes naturally with station-tier gating: a 6-ingredient recipe physically requires a 6-slot (Infused) Apothecary regardless of what's in it. Petal colors and non-rune fillers are picked thematically — Botania's colors where a real recipe exists to anchor to, MAM-original picks (vanilla items, not invented ones) where it doesn't. Exact non-rune filler items and mana costs are a directional first pass here, not a final balancing pass — same maturity level [[magic/25_runes]] already claims for its own T1/T2 ingredients ("TBD — balancing pass"). (2026-07-02)

**Q:** Does this mean T2/T3 flowers can't be crafted until the Altar and rune items exist?
**A:** Their recipe is blocked on that, yes (see Validation) — but their block/item/mechanic implementation isn't. A T2/T3 flower can exist, tick, and generate mana in creative-tab testing before its survival recipe is craftable, same as how Daybloom/Endoflame/Hydroangeas exist today with no recipe at all. (2026-07-02)

**Implementation pattern:** recipes aren't hand-authored JSON — they're built in `MamRecipeProvider` as `new ApothecaryRecipe(List<Ingredient> ingredients, Ingredient reagent, ItemStack output)`, same as the existing `apothecaryPureDaisy()` method. Reagent is `Ingredient.of(Tags.Items.SEEDS)` (`#c:seeds`) — confirmed from that method, resolving the open question this doc previously carried about the reagent tag. Petals are individual dyed items via `VerdantFlowers.PETALS.get(DyeColor.X)`.

---

## T1 — shipped

### Daybloom (`mam:daybloom`)

- **Trigger:** daytime (`dayTime % 24000 < 12000`) and the flower can see the sky.
- **Rate:** +1 mana/tick while the condition holds.
- **Max energy:** 900.
- **Recipe:** 4× yellow petal + seed. No Botania reference exists — current Botania has no generating Daybloom class at all (decorative "motif" block only in this version) — yellow chosen thematically (day/sun) since there's no recipe to port.

### Endoflame (`mam:endoflame`)

- **Trigger:** burns nearby item entities as furnace fuel, scanning a 3-block radius.
- **Rate:** on picking up a fuel item, burns for `min(32000, itemBurnTime) / 2` ticks, generating +3 mana every 2 ticks while burning (consumes one item per pickup).
- **Max energy:** 300.
- **Recipe:** 2× brown + 1× red + 1× light gray petal + seed (Botania's real recipe — petals-only already, no rune involved).

### Hydroangeas (`mam:hydroangeas`)

- **Trigger:** raining directly above the flower, or a water source adjacent horizontally; gated to every other tick.
- **Rate:** +1 mana per qualifying tick (~every 2 ticks).
- **Max energy:** 900.
- **Recipe:** 2× blue + 2× cyan petal + seed (Botania's real recipe — petals-only already, no rune involved).

---

## T2 — established

Recipe shape: 4 petals + 1 rune (T1 elemental) + 1 filler + seed = 6 ingredients + seed.

### Thermalily

Lava/water contact generator — shares the fluid-adjacency pattern with Hydroangeas (opposite fluid tag).

- **Trigger:** adjacent `FluidTags.LAVA` source.
- **Numbers:** `maxMana` 45/action, base cooldown 600 ticks, actual cooldown rolled from a lookup table (`COOLDOWN_ROLL_PDF`) scaled ×400.
- **Recipe:** 2× red + 2× orange petal + **Rune of Flow** + Blaze Powder + seed. Flow ("motion, transfer, mana current") fits fluid contact; Blaze Powder for the heat side of a lava/water flower.

### Rosa Arcana

Absorbs XP orbs.

- **Trigger:** XP orb entities within range 1.
- **Numbers:** 50 mana per orb absorbed.
- **Recipe:** 2× pink + 2× purple petal + **Rune of Mana** + Amethyst Shard + seed. Mana ("living magical current") is the most direct fit of any rune to any flower in this set — Rosa Arcana absorbs raw magic directly, same as Botania's own `rune_mana` pick. Amethyst Shard for the arcane-crystal flavor.

### Munchdew

Poisons and eats mobs.

- **Trigger:** living entities within an 8-block horizontal / 16-block vertical range; applies Poison, then harvests a "leaf" once the target is weakened.
- **Numbers:** 160 mana per leaf, cooldown 1600 ticks.
- **Recipe:** 2× lime + 2× red petal + **Rune of Death** + Rotten Flesh + seed. Death ("decay, ending, sacrifice") fits Verdant's one carnivorous flower — the odd one out thematically, by design. Rotten Flesh for predation.

### Narslimmus

Eats slimes.

- **Trigger:** `Slime` entities within range 2.
- **Numbers:** base 1200 mana (300 in Garden of Glass-style low-resource contexts), scales `×2^slimeSize`.
- **Recipe:** 2× lime + 2× green petal + **Rune of Chaos** + Slimeball + seed. Chaos ("disruption, wild magic, entropy") fits an unstable ooze; Slimeball is the obvious filler.

---

## T3 — advanced

Recipe shape: 4 petals + 1 rune + 3 filler + seed = 8 ingredients + seed. The three flowers with deferred mechanics (Entropinnyum, Dandelifeon, Shulk Me Not) use **T3a Grand Force** runes rather than T2 Concept runes — they're the true endgame of this set even within T3, and the rune tier reflects that.

### Kekimurus

Eats nearby cake blocks.

- **Trigger:** scans an 11×11×11 volume every 80 ticks for a `CakeBlock`; consumes one cake bite per trigger.
- **Numbers:** 1800 mana per bite, range 5 blocks.
- **Recipe:** 2× white + 2× orange petal + **Rune of Growth** + Wheat + Sugar + Egg + seed. Growth ("cultivation and expansion") fits insatiable consumption; the filler is literally three of cake's own four ingredients — the flower's crafting recipe hints at its own hunger.

### Gourmaryllis

Eats food items.

- **Trigger:** edible item entities within range 1.
- **Numbers:** `foodValue² × 70` mana, with a streak multiplier climbing up to 1.8× for consecutive feeds; `MAX_FOOD_VALUE` cap of 12.
- **Recipe:** 2× light gray + 2× yellow petal + **Rune of Decay** + 2× Golden Carrot + Honey Bottle + seed. Decay ("uncontrolled breakdown") fits digestion — food broken down into raw magical energy. Golden Carrot/Honey Bottle for rich food.

### Spectrolus

Cycles dye colors, eats matching wool/sheep.

- **Trigger (deferred, see [[../todo/03_complex-generating-flower-mechanics]]):** rotating `DyeColor` sequence; matching sheep/wool consumed.
- **Numbers (Botania reference):** `WOOL_GEN` 1200, `SHEEP_GEN` 5000, `BABY_SHEEP_GEN` 1, range 1.
- **Recipe:** 1 each red + green + blue + white petal (full spectrum, one of each rather than pairs) + **Rune of Binding** + 3× Glass + seed. Binding ("structure imposed on wild things") fits a precise, locked color sequence; Glass for light/prism.

### Entropinnyum

Detects nearby TNT explosions.

- **Trigger (deferred):** `PrimedTnt` entities within range 12.
- **Numbers (Botania reference):** full `getMaxMana()` per explosion.
- **Recipe:** 2× red + 2× gray petal + **Rune of Entropy** (T3a) + 3× Gunpowder + seed. Entropy ("structure dissolving; the unravelling of order") — an explosion detector needs no better name.

### Dandelifeon

Runs Conway's Game of Life on a flower grid.

- **Trigger (deferred):** cellular-automaton tick loop, range/speed 12/10.
- **Numbers (Botania reference):** 60 mana/generation, cap 100 generations.
- **Recipe:** 2× purple + 1× lime + 1× green petal + **Rune of the Cycle** (T3a) + 3× Redstone + seed. Cycle ("life feeds death feeds life") for a Game-of-Life flower; Redstone for the computation/logic flavor.

### Shulk Me Not

Levitates and eats Shulkers.

- **Trigger (deferred):** Shulker entities within radius 8, levitation effect applied.
- **Numbers (Botania reference):** 75,000 mana per shulker — the single largest payout in this entire set.
- **Recipe:** 2× purple + 2× magenta petal + **Rune of Transcendence** (T3a) + 3× Ender Pearl + seed. Transcendence ("ascension through life and living mana") for the levitating End flower; Ender Pearl for the otherworldly flavor.

---

## Validation

- `todo` — Daybloom: Apothecary recipe (4× yellow + seed)
- `todo` — Endoflame: Apothecary recipe (2 brown + 1 red + 1 light gray + seed)
- `todo` — Hydroangeas: Apothecary recipe (2 blue + 2 cyan + seed)
- `todo` — Thermalily: block, item, block entity mechanic
- `blocked` — Thermalily: Apothecary recipe (needs `rune_flow`, [[magic/25_runes]] item registration not started)
- `todo` — Rosa Arcana: block, item, block entity mechanic
- `blocked` — Rosa Arcana: Apothecary recipe (needs `rune_mana`)
- `todo` — Munchdew: block, item, block entity mechanic
- `blocked` — Munchdew: Apothecary recipe (needs `rune_death`)
- `todo` — Narslimmus: block, item, block entity mechanic
- `blocked` — Narslimmus: Apothecary recipe (needs `rune_chaos`)
- `todo` — Kekimurus: block, item, block entity mechanic
- `blocked` — Kekimurus: Apothecary recipe (needs `rune_growth`, [[magic/20_altar]] Infused Altar not implemented)
- `todo` — Gourmaryllis: block, item, block entity mechanic
- `blocked` — Gourmaryllis: Apothecary recipe (needs `rune_decay`)
- `todo` — Spectrolus: block, item (mechanic deferred, [[../todo/03_complex-generating-flower-mechanics]])
- `blocked` — Spectrolus: Apothecary recipe (needs `rune_binding`)
- `todo` — Entropinnyum: block, item (mechanic deferred)
- `blocked` — Entropinnyum: Apothecary recipe (needs `rune_entropy`, T3a — Sacred Altar not implemented)
- `todo` — Dandelifeon: block, item (mechanic deferred)
- `blocked` — Dandelifeon: Apothecary recipe (needs `rune_cycle`, T3a)
- `todo` — Shulk Me Not: block, item (mechanic deferred)
- `blocked` — Shulk Me Not: Apothecary recipe (needs `rune_transcendence`, T3a)
- `todo` — `botania:generating_special_flowers` tag updated with the 6 T2/T3 non-deferred flowers (per CLAUDE.md Botania-compat policy)
- `todo` — Site docs (`site/content/`) for all flowers in this doc
- `todo` — Patchouli book entries — first-ever category in `data/mam/patchouli_books/guide/` (currently an empty shell, no categories exist)
- `todo` — Ponder scenes for T1/T2 flowers and any T3 flower with a working mechanic
