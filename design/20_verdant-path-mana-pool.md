---
type: design
status: draft
last-updated: 2026-06-29
links: "[[20_verdant-path]], [[20_verdant-path-quipment]], [[21_verdant-implementation-status]]"
---

# Verdant Path — Mana Pools

Mana Pools are the central storage and processing hubs of Verdant infrastructure. They receive mana from flowers, power infusion recipes, and repair mana-integrated gear. Three tiers of pool exist — each with a larger capacity, unlocking higher-tier ingot infusion.

---

## Pool Tiers

| Pool              | Capacity    | Ingot Unlocked | Infusion Cost |
|-------------------|-------------|----------------|---------------|
| Mana Pool         | 1,000,000   | Mana Ingot     | 750,000 (75%) |
| Infused Mana Pool | 4,000,000   | Infused Ingot  | 3,000,000 (75%) |
| Sacred Mana Pool  | 16,000,000  | Sacred Ingot   | 12,000,000 (75%) |

Capacity numbers are a starting point — adjust in the balancing pass. The 4× multiplier per tier is the invariant; the exact values may shift. The 75% rule must hold across all tiers.

---

## Pool Crafting Recipes

The Tier 1 pool is crafted directly from Living Rock. Tier 2 and 3 pools are produced by combining a **Rune** with the existing pool at a crafting table — breaking the circular dependency on higher-tier Living Rock entirely.

| Output            | Recipe                                              | Status      |
|-------------------|-----------------------------------------------------|-------------|
| Mana Pool         | Living Rock — U shape (crafting table)              | ✅ decided  |
| Infused Mana Pool | Rune of Infusion + Mana Pool item (crafting table)  | ✅ decided  |
| Sacred Mana Pool  | Rune of the Sacred + Infused Mana Pool item (crafting table) | ✅ decided |

Tier 1 pool — U shape, 8× Living Rock:

```
R R R
R . R
R R R   R = Living Rock   yields 1× Mana Pool
```

*Reference: `Botania/Xplat/src/generated/resources/data/botania/recipes/mana_pool.json`*

Tier 2/3 upgrade — pick up the existing pool (breaks as an item, stored mana lost), combine with the matching rune at a crafting table:

```
[ Rune ]  [ Pool item ]  →  [ Next-tier Pool item ]
```

The mana loss on pickup is an intentional cost — you are committing your infrastructure to the next tier.

---

## Rune Production

Runes are crafted at the **Altar** — the Verdant ritual station equivalent to Botania's Runic Altar.

**Mechanic:** Place ingredients around the altar, then drop a **Living Rock** as the final trigger. When a nearby Mana Pool has sufficient mana, the altar automatically consumes the mana and ingredients and ejects the rune. No wand or manual activation — mana availability is the only gate.

| Rune                | Trigger     | Mana Cost | Other Ingredients |
|---------------------|-------------|-----------|-------------------|
| Rune of Infusion    | Living Rock | TBD       | TBD               |
| Rune of the Sacred  | Living Rock | TBD       | TBD               |

Rune ingredients and mana costs are TBD — set during balancing pass. Both runes must be craftable from the tier *below* their target pool (T1 materials for Rune of Infusion, T2 materials for Rune of the Sacred), so no circular dependency.

Full Altar design: `design/20_verdant-path-altar.md`

---

## Infusion Mechanic

Drop an item on top of a Mana Pool. If the pool has enough mana for the recipe and the correct tier pool is used, it consumes the mana and ejects the output item.

### Flow

1. Player drops item (e.g. Block of Copper) onto the pool surface
2. Pool detects the item entity via collision / nearby-entity scan each tick
3. Pool checks: is this item a valid infusion input? Does the pool have enough mana?
4. If yes: consume mana, remove item entity, spawn output item entity on top of pool
5. If no: item sits on pool surface harmlessly (passes through / floats)

### Infusion Recipes

Pool energy capacity gates recipes. At T3, energy **type** also matters — Sacred Mana Pool accepts Mana only, Desecrated Pool accepts Nox only (see `design/00_magic-energy.md`).

Stone and wood follow a **stepped chain** — Infused tier must be produced before Sacred or Desecrated. Ingots and gems do not (each uses a different base material). Sacred and Desecrated are parallel T3 branches from Infused.

| Input                | Min Capacity       | Cost       | Output                      |
|----------------------|--------------------|------------|-----------------------------|
| Block of Copper      | ≥1,000,000 Mana    | 750,000    | 1× Mana Ingot               |
| Block of Gold        | ≥4,000,000 Mana    | 3,000,000  | 1× Infused Ingot            |
| Block of Diamond     | ≥16,000,000 Mana   | 12,000,000 | 1× Sacred Ingot             |
| Block of Diamond     | ≥16,000,000 Nox    | 12,000,000 | 1× Desecrated Ingot         |
| Living Rock          | ≥4,000,000 Mana    | 3,000,000  | 1× Infused Living Rock      |
| Infused Living Rock  | ≥16,000,000 Mana   | 12,000,000 | 1× Sacred Living Rock       |
| Infused Living Rock  | ≥16,000,000 Nox    | 12,000,000 | 1× Desecrated Living Rock   |
| Livingwood           | ≥4,000,000 Mana    | 3,000,000  | 1× Infused Livingwood       |
| Infused Livingwood   | ≥16,000,000 Mana   | 12,000,000 | 1× Sacred Livingwood        |
| Infused Livingwood   | ≥16,000,000 Nox    | 12,000,000 | 1× Desecrated Livingwood    |
| Diamond              | ≥1,000,000 Mana    | 750,000    | 1× Mana Diamond             |
| Diamond              | ≥4,000,000 Mana    | 3,000,000  | 1× Infused Diamond          |
| Diamond              | ≥16,000,000 Mana   | 12,000,000 | 1× Sacred Diamond           |
| Ender Pearl          | ≥1,000,000 Mana    | 750,000    | 1× Mana Pearl               |
| Ender Pearl          | ≥4,000,000 Mana    | 3,000,000  | 1× Infused Pearl            |
| Ender Pearl          | ≥16,000,000 Mana   | 12,000,000 | 1× Sacred Pearl             |
| Gunpowder **or** Redstone **or** Glowstone Dust **or** Sugar | ≥1,000,000 Mana | 500 | 1× Mana Dust |

### The double lock

Infused and Sacred Living Rock have no crafting recipe — the **only** production path is pool infusion. This creates two compounding gates for Sacred Living Rock specifically:

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

| Pool              | Stone Color     | Fluid Color     | Notes                        |
|-------------------|-----------------|-----------------|------------------------------|
| Mana Pool         | Living Rock grey | Teal/cyan       | Matches current T1 texture   |
| Infused Mana Pool | Infused Rock blue-grey | Deep blue/purple | More saturated, darker stone |
| Sacred Mana Pool  | Sacred Rock gold-white | Gold/white glow | Bright, almost luminous      |

Mana fill is shown as a fluid layer inside the pool basin — height rises with fill %. Comparator output: 0–15 linear with fill %.

---

## Open Questions

- [x] **Mana Pool recipe shape** — ring/U shape, 8× Living Rock (same shape for all tiers). WP-C unblocked.
- [x] **Infused / Sacred pool recipes** — Rune + existing pool item at crafting table. Unblocked.
- [ ] **Fluid rendering** — custom renderer or repurposed water? Decide before Dev work on Tier 2/3 visual.
- [x] **Pool upgrade path** — pick up existing pool (mana lost), combine with rune at crafting table → next tier pool. Not in-place.

---

## Work Packages

### WP-A — Tier 1 Infusion Mechanic (unblocked)

```
Role: Dev
Design doc: design/20_verdant-path-mana-pool.md § Infusion Mechanic
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

### WP-B — Tier 2/3 Pool Registration (unblocked; recipes blocked)

```
Role: Dev
Design doc: design/20_verdant-path-mana-pool.md § Pool Tiers
File targets:
  - src/main/java/org/mjli/mam/verdant/VerdantMana.java — register InfusedManaPool, SacredManaPool blocks
  - src/main/java/org/mjli/mam/MamBlockEntities.java — register block entity types for T2/T3
  - src/main/java/org/mjli/mam/block_entity/mana/ — InfusedManaPoolBlockEntity, SacredManaPoolBlockEntity (subclass ManaPoolBlockEntity, override MAX_MANA)
  - src/main/resources/assets/mam/ — models and textures for T2/T3 pools
Acceptance criteria:
  - [ ] Infused Mana Pool and Sacred Mana Pool place and break without error
  - [ ] Each has its own texture/model distinct from Tier 1
  - [ ] MAX_MANA is 4,000,000 for T2 and 16,000,000 for T3
  - [ ] Comparator output scales correctly for each tier's capacity
  - [ ] Infusion mechanic (from WP-A) works for T2 → Infused Ingot, T3 → Sacred Ingot
Out of scope: crafting recipes for the pools themselves (blocked on Living Rock design)
```

### WP-C — Tier 1 Pool Recipe (unblocked)

```
Role: Dev
Design doc: design/20_verdant-path-mana-pool.md § Pool Crafting Recipes
File targets:
  - src/main/resources/data/mam/recipes/ — mana_pool crafting recipe JSON
Acceptance criteria:
  - [ ] Mana Pool is craftable in a crafting table from Living Rock (shape TBD — see Open Questions)
  - [ ] Recipe appears in JEI/REI
Out of scope: Infused/Sacred pool recipes
```

---

## Status

| Item                              | Status |
|-----------------------------------|--------|
| Tier 1 pool block + block entity  | ✅ implemented |
| Tier 1 capacity (1M)              | ✅ implemented |
| Tier 2/3 block registration       | ⬜ WP-B |
| Desecrated Pool block registration | ⬜ planned |
| Infusion mechanic                 | ⬜ WP-A |
| Pool infusion recipes (data)      | ⬜ WP-A |
| Desecrated infusion recipes (data) | ⬜ planned |
| Tier 1 pool crafting recipe       | ⬜ WP-C (shape TBD) |
| Tier 2/3 pool crafting recipes    | ⬜ blocked — Living Rock design |
| Desecrated Pool crafting recipe   | ⬜ blocked — Desecrated Living Rock needed |
