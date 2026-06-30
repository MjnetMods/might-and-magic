---
type: design
status: draft
last-updated: 2026-06-29
links: "[[20_verdant-path]], [[20_verdant-path-quipment]], [[21_verdant-implementation-status]], [[magic/00_energy]], [[magic/10_apothecary]], [[magic/20_altar]], [[magic/25_runes]]"
---

# Verdant Path — Mana Pools

Mana Pools are the central storage and processing hubs of Verdant infrastructure. They receive mana from flowers, power infusion recipes, and repair mana-integrated gear. 
Three tiers of pool exist — each with a larger capacity, unlocking higher-tier ingot infusion.

---

## Pool Tiers

| Pool                | Capacity   | Ingot Unlocked  | Infusion Cost    |
|---------------------|------------|-----------------|------------------|
| Mana Pool           | 1,000,000  | Mana Ingot      | 750,000 (75%)    |
| Infused Mana Pool   | 4,000,000  | Infused Ingot   | 3,000,000 (75%)  |
| Sacred Mana Pool    | 16,000,000 | Sacred Ingot    | 12,000,000 (75%) |
| Desecrated Mana Pool | 16,000,000 | Desecrated Ingot | 12,000,000 (75%) |

Capacity numbers are a starting point — adjust in the balancing pass. The 4× multiplier per tier is the invariant; the exact values may shift. The 75% rule must hold across all tiers.

---

## Pool Crafting Recipes

The Tier 1 pool is crafted directly from Living Rock. Tier 2/3 pools have **two** crafting paths:

1. **Bootstrap path — Rune + Pool item.** A Tier 1 pool only holds 1,000,000 mana, but infusing Living Rock into Infused Living Rock needs ≥4,000,000 mana present at once — impossible with only a Tier 1 pool. Combining a **Rune** with the existing pool at a crafting table upgrades the pool's capacity directly, without needing any higher-tier Living Rock. This is the only way to obtain your *first* pool of a given tier.
2. **Direct path — U shape from tier-matched Living Rock.** Once you own one Infused/Sacred pool, its higher capacity lets you infuse more Living Rock into that tier, so additional copies of the pool can be built the same way as Tier 1 — U shape, tier-matched rock.

Both paths are intentional, not redundant: the Rune path breaks the circular dependency for the first pool of a tier; the U-shape path is the normal repeat-build path afterward.

Tier 1 pool — U shape, 7× Living Rock:

```
R . R
R . R
R R R   R = Living Rock   yields 1× Mana Pool
```

*Reference: `Botania/Xplat/src/generated/resources/data/botania/recipes/mana_pool.json`*

Tier 2/3 upgrade — pick up the existing pool (breaks as an item, stored mana lost), combine with the matching rune at a crafting table:

```
[ Rune ]  [ Pool item ]  →  [ Next-tier Pool item ]
```

The mana loss on pickup is an intentional cost — you are committing your infrastructure to the next tier.


| Output            | Recipe                                                          | Status    |
|-------------------|-----------------------------------------------------------------|-----------|
| Mana Pool         | Living Rock — U shape (crafting table)                          | ✅ implemented |
| Infused Mana Pool | Bootstrap: Rune of Infusion + Mana Pool item (crafting table)   | ✅ decided, blocked on Altar/Runes |
| Infused Mana Pool | Direct: Infused Living Rock — U shape (crafting table)          | ✅ implemented |
| Sacred Mana Pool  | Bootstrap: Rune of the Sacred + Infused Mana Pool item (crafting table) | ✅ decided, blocked on Altar/Runes |
| Sacred Mana Pool  | Direct: Sacred Living Rock — U shape (crafting table)           | ✅ implemented |
| Desecrated Mana Pool | Bootstrap: Rune of the Desecrated + Infused Mana Pool item (crafting table) | ✅ decided, blocked on Altar/Runes |
| Desecrated Mana Pool | Direct: Desecrated Living Rock — U shape (crafting table)    | ✅ implemented |

---

## Rune Production

Runes are crafted at the **Altar** — the Verdant ritual station equivalent to Botania's Runic Altar.

**Mechanic:** Place ingredients around the altar, then drop a **Living Rock** as the final trigger. When a nearby Mana Pool has sufficient mana, the altar automatically consumes the mana and ingredients and ejects the rune. No wand or manual activation — mana availability is the only gate.

| Rune (`mam:`)     | Trigger     | Energy Cost  | Other Ingredients                                                              |
|-------------------|-------------|--------------|----------------------------------------------------------------------------------|
| `rune_infusion`   | Living Rock | ~6,000 Mana  | 2× Living Rock + 1× `#mam:mana_gems` + 1× Rune of Flow                          |
| `rune_sacred`     | Living Rock | ~10,000 Mana | 2× Infused Living Rock + Rune of Mana + Rune of Binding + 1× `#mam:infused_gems`   |
| `rune_desecrated` | Living Rock | ~10,000 Nox  | 2× Infused Living Rock + Rune of Decay + Rune of Binding + 1× `#mam:infused_gems`  |

Energy costs are approximate — tune during balancing pass. All three runes use only tier-matched materials to avoid circular dependency (Rune of Infusion uses T1 only; Rune of the Sacred and Rune of the Desecrated use T2 only, available from the Infused Pool you already have). Sacred and Desecrated are parallel branches off the same Infused base, differentiated by their T2 input rune (Rune of Mana for Sacred, Rune of Decay for Desecrated — see [[magic/25_runes]]) and by drawing Mana vs Nox from the Altar's source pool, consistent with the T3 energy-alignment rule ([[magic/00_energy]]). Full recipe spec: `design/magic/25_runes.md § Infrastructure Runes`.

Full Altar design: `design/magic/20_altar.md`

---

## Infusion Mechanic

Drop an item on top of a Mana Pool. If the pool has enough mana for the recipe, it consumes the mana and ejects the output item.

### Flow

1. Player drops item (e.g. Block of Copper) onto the pool surface
2. Pool detects the item entity via collision / nearby-entity scan each tick
3. Pool checks: is this item a valid infusion input? Does the pool have enough mana?
4. If yes: consume mana, remove item entity, spawn output item entity on top of pool
5. If no: item sits on pool surface harmlessly (passes through / floats)

### Infusion Recipes

Pool energy capacity gates recipes. At T3, energy **type** also matters — Sacred Mana Pool accepts Mana only, Desecrated Mana Pool accepts Nox only (see `design/magic/00_energy.md`).

Stone and wood follow a **stepped chain** — Infused tier must be produced before Sacred or Desecrated. Ingots and gems do not (each uses a different base material). Sacred and Desecrated are parallel T3 branches from Infused.

| Input                                                        | Min Capacity     | Cost       | Output                    |
|--------------------------------------------------------------|------------------|------------|---------------------------|
| Block of Copper                                              | ≥1,000,000 Mana  | 750,000    | 1× Mana Ingot             |
| Block of Gold                                                | ≥4,000,000 Mana  | 3,000,000  | 1× Infused Ingot          |
| Block of Diamond                                             | ≥16,000,000 Mana | 12,000,000 | 1× Sacred Ingot           |
| Block of Diamond                                             | ≥16,000,000 Nox  | 12,000,000 | 1× Desecrated Ingot       |
| Living Rock                                                  | ≥4,000,000 Mana  | 3,000,000  | 1× Infused Living Rock    |
| Infused Living Rock                                          | ≥16,000,000 Mana | 12,000,000 | 1× Sacred Living Rock     |
| Infused Living Rock                                          | ≥16,000,000 Nox  | 12,000,000 | 1× Desecrated Living Rock |
| Livingwood                                                   | ≥4,000,000 Mana  | 3,000,000  | 1× Infused Livingwood     |
| Infused Livingwood                                           | ≥16,000,000 Mana | 12,000,000 | 1× Sacred Livingwood      |
| Infused Livingwood                                           | ≥16,000,000 Nox  | 12,000,000 | 1× Desecrated Livingwood  |
| Diamond                                                      | ≥1,000,000 Mana  | 750,000    | 1× Mana Diamond           |
| Diamond                                                      | ≥4,000,000 Mana  | 3,000,000  | 1× Infused Diamond        |
| Diamond                                                      | ≥16,000,000 Mana | 12,000,000 | 1× Sacred Diamond         |
| Diamond                                                      | ≥16,000,000 Nox  | 12,000,000 | 1× Desecrated Diamond     |
| Ender Pearl                                                  | ≥1,000,000 Mana  | 750,000    | 1× Mana Pearl             |
| Ender Pearl                                                  | ≥4,000,000 Mana  | 3,000,000  | 1× Infused Pearl          |
| Ender Pearl                                                  | ≥16,000,000 Mana | 12,000,000 | 1× Sacred Pearl           |
| Ender Pearl                                                  | ≥16,000,000 Nox  | 12,000,000 | 1× Desecrated Pearl       |
| Gunpowder **or** Redstone **or** Glowstone Dust **or** Sugar | ≥1,000,000 Mana  | 500        | 1× Mana Dust              |

### The double lock

Infused and Sacred/Desecrated Living Rock have no crafting recipe — the **only** production path is pool infusion. This creates two compounding gates for Sacred Living Rock specifically:

1. **Capacity lock.** Infused Living Rock costs 3,000,000 mana (≥4M pool). Sacred Living Rock costs 12,000,000 mana (≥16M pool). An undersized pool cannot run these recipes regardless of fill level.
2. **Stepped chain.** Sacred Living Rock takes **Infused Living Rock** as input — not base Living Rock. You must produce the Infused tier first. Total mana spend to reach Sacred stone or wood: 3M + 12M = 15M. This is intentional.

Each conversion also costs 75% of a pool's capacity, so you cannot spam either step — the pool must refill between conversions.

The result: Sacred Living Rock requires a working ≥4M pool (to produce Infused) and a ≥16M pool (to elevate to Sacred). Both capacity thresholds must be met in sequence.

### Implementation notes

- Scan nearby item entities once per second (every 20 ticks) to avoid per-tick cost
- Use a data-driven recipe type (`mam:pool_infusion`) so new recipes can be added without code changes
- Recipe ingredients: item tag or item, pool tier requirement, mana cost, output
- Reference: Botania `ManaPoolBlockEntity` item pickup logic in `/Users/mannil/mcmod/Botania`

---

## Visual Design

| Pool                 | Stone Color                |
|----------------------|----------------------------|
| Mana Pool            | Living Rock (Stone Grey)   |
| Infused Mana Pool    | Infused Rock (White)       |
| Sacred Mana Pool     | Infused Rock (Green tint)  |
| Desecrated Mana Pool | Infused Rock (Purple tint) |

                                                        
Mana fill is shown as a fluid layer inside the pool basin — height rises with fill %. Comparator output: 0–15 linear with fill %.

Color of fluid is green for mana, purple for nox

---

## Open Questions

- [x] **Mana Pool recipe shape** — ring/U shape, 7× Living Rock (same shape for all tiers). WP-C unblocked.
- [x] **Infused / Sacred pool recipes** — Rune + existing pool item at crafting table. Unblocked.
- [ ] **Fluid rendering** — custom renderer or repurposed water? Decide before Dev work on Tier 2/3 visual.
- [x] **Pool upgrade path** — pick up existing pool (mana lost), combine with rune at crafting table → next tier pool. Not in-place.

---

## Work Packages

### WP-A — Tier 1 Infusion Mechanic (unblocked)

```
Role: Dev
Design doc: design/magic/15_mana-pool.md § Infusion Mechanic
File targets:
  - src/main/java/org/mjli/mam/block_entity/mana/ManaPoolBlockEntity.java — add item-scan + infusion logic
  - src/main/java/org/mjli/mam/recipe/ — new PoolInfusionRecipe type (data-driven)
  - src/main/resources/data/mam/recipes/ — add copper_block → mana_ingot infusion recipe JSON
Acceptance criteria:
  - [ ] Drop a Block of Copper on a full Mana Pool → Mana Ingot ejects, pool loses 750,000 mana
  - [ ] Drop same block on a pool with < 750,000 mana → nothing happens, block stays
  - [ ] Drop an unrecognised item → passes through, no mana consumed
  - [ ] Recipe is in a JSON file, not hardcoded
Out of scope: Infused/Sacred pools, Living Rock recipes, gear repair mechanic
```

### WP-B — Tier 2/3 Pool Registration (done; textures + infusion linkage pending)

```
Role: Dev
Design doc: design/magic/15_mana-pool.md § Pool Tiers
File targets:
  - src/main/java/org/mjli/mam/verdant/VerdantMana.java — register InfusedManaPool, SacredManaPool, DesecratedManaPool blocks
  - src/main/java/org/mjli/mam/MamBlockEntities.java — register block entity types for T2/T3
  - src/main/java/org/mjli/mam/block_entity/mana/ManaPoolBlockEntity.java — tiered via constructor (capacity, ManaEnergyType), not subclassing
  - src/main/resources/assets/mam/ — models and textures for T2/T3 pools
Acceptance criteria:
  - [x] Infused/Sacred/Desecrated Mana Pool place and break without error
  - [ ] Each has its own texture/model distinct from Tier 1 (placeholder: reuses Tier 1 art)
  - [x] MAX_MANA is 4,000,000 for T2 and 16,000,000 for T3
  - [x] Comparator output scales correctly for each tier's capacity
  - [ ] Infusion mechanic (from WP-A) works for T2 → Infused Ingot, T3 → Sacred Ingot
Out of scope: crafting recipes for the pools themselves (now covered by WP-C)
```

### WP-C — Pool Crafting Recipes (done)

```
Role: Dev
Design doc: design/magic/15_mana-pool.md § Pool Crafting Recipes
File targets:
  - src/main/java/org/mjli/mam/infrastructure/datagen/MamRecipeProvider.java — mana_pool, infused_mana_pool, sacred_mana_pool, desecrated_mana_pool recipes
Acceptance criteria:
  - [x] Mana Pool is craftable in a crafting table from Living Rock (U shape, 7× — see § Pool Crafting Recipes)
  - [x] Infused/Sacred/Desecrated pools craftable from tier-matched Living Rock (same U shape, direct path)
  - [ ] Recipe appears in JEI/REI (not yet verified in-game)
Out of scope: bootstrap recipe (Rune + Pool item) — blocked on Altar/Runes
```

---

## Status

| Item                              | Status |
|-----------------------------------|--------|
| Tier 1 pool block + block entity  | ✅ implemented |
| Tier 1 capacity (1M)              | ✅ implemented |
| Tier 2/3 block registration       | ✅ implemented (WP-B) |
| Desecrated Mana Pool block registration | ✅ implemented |
| Tier 2/3 capacity (4M/16M) + energy type (Mana/Nox) | ✅ implemented |
| Tier 1 pool crafting recipe       | ✅ implemented (WP-C, 7× Living Rock) |
| Tier 2/3 pool crafting recipes    | ✅ implemented (direct path, tier-matched Living Rock) |
| Desecrated Mana Pool crafting recipe | ✅ implemented |
| Bootstrap pool recipe (Rune + Pool item) | ⬜ blocked — Altar/Runes |
| Tiered pool textures/models        | ⬜ placeholder — reuse Tier 1 art |
| Infusion mechanic                 | ⬜ WP-A |
| Pool infusion recipes (data)      | ⬜ WP-A |
| Desecrated infusion recipes (data) | ⬜ planned |
